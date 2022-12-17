package euphoria;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonSyntaxException;
import euphoria.packet_types.nick;
import euphoria.packet_types.ping_reply;
import euphoria.types.Message;
import euphoria.types.PacketType;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class RoomConnection extends WebSocketClient {

    HashMap<String,CompletableFuture<EuphoriaPacket>> pendingReplies = new HashMap<>();
    EuphoriaBot bot;
    boolean loggedIn=false;


    public RoomConnection(URI serverUri, EuphoriaBot bot) {
        super(serverUri);
        this.bot=bot;

    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);

    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
        try {
            EuphoriaPacket packet = bot.gson.fromJson(message, EuphoriaPacket.class);
            if (packet.type != null) {
                onPacket(packet);
            }
        }catch (JsonSyntaxException e){
            System.out.println(e.getMessage());
            System.out.println("Received malformed packet "+message);
        }
    }
    public void onPacket(EuphoriaPacket packet){
        if(packet.id!=null){
            if (pendingReplies.containsKey(packet.id)) {
                pendingReplies.get(packet.id).complete(packet);
                pendingReplies.remove(packet.id);
            }
        }
        switch (PacketType.fromName(packet.type)) {
            case PING_EVENT ->
                    sendPacket(new EuphoriaPacket(PacketType.PING_REPLY, new ping_reply(packet.data.get("time").getAsLong())));
            case HELLO_EVENT -> {
                loggedIn = true;
                setName(bot.name);
                bot.onJoinRoom(this);
            }
            case SEND_EVENT -> bot.onMessage(bot.gson.fromJson(packet.data, Message.class), this);
            default -> System.out.println("Unimplemented/unrecognized packet: " + bot.gson.toJson(packet)+" with type "+PacketType.fromName(packet.type));
        }


    }
    public void setName(String name){
        sendPacket(new EuphoriaPacket(PacketType.NICK,new nick(name)));
    }
    void sendPacket(EuphoriaPacket packet){
        System.out.println("sent packet "+bot.gson.toJson(packet));
        send(bot.gson.toJson(packet));
    }

    CompletableFuture<EuphoriaPacket> sendCommand(PacketType type, Object data){
        //todo check if command type
        EuphoriaPacket packet = new EuphoriaPacket(UUID.randomUUID().toString(),type,data);
        pendingReplies.put(packet.id, new CompletableFuture<>());
        sendPacket(packet);
        return pendingReplies.get(packet.id);
    }

    /**
     * Sends a Message
     * @param message Message to be sent
     * @return Server replied message object
     */
    public CompletableFuture<Message> sendEuphoriaMessage(Message message){
        return sendCommand(PacketType.SEND,message).thenApply(packet -> bot.gson.fromJson(packet.data, Message.class));
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}