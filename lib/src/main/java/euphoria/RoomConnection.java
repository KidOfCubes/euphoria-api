package euphoria;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonSyntaxException;
import euphoria.packet_types.hello_event;
import euphoria.packet_types.nick;
import euphoria.packet_types.ping_reply;
import euphoria.types.Message;
import euphoria.types.PacketType;
import euphoria.types.SessionView;
import euphoria.types.Snowflake;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class RoomConnection extends WebSocketClient {

    HashMap<String,CompletableFuture<EuphoriaPacket>> pendingReplies = new HashMap<>();
    EuphoriaBot bot;

    SessionView sessionView;
    boolean loggedIn=false;


    public RoomConnection(URI serverUri, EuphoriaBot bot) {
        super(serverUri);
        this.bot=bot;

    }

    /**
     * Attempts to retrieve the room from the websockets endpoint URL
     * (Assuming format of wss://example.com/foo/room/ROOMNAME/bar)
     * @return The room name if it could be retrieved, or the entire url
     */
    public String roomName(){
        String path = getURI().getRawPath();
        if(path!=null) {
            String[] pathArray = path.split("/");
            String roomName = null;
            for(int i=0;i<pathArray.length;i++){
                if(roomName!=null){
                    roomName=pathArray[i];
                    break;
                }
                if(pathArray[i].equalsIgnoreCase("room")){
                    roomName="";
                }
            }
            if(roomName!=null){
                return roomName;
            }
        }
        return getURI().toASCIIString();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {}

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(remote){
            reconnect();
        }
    }

    @Override
    public void onMessage(String text) {
        try {
            EuphoriaPacket packet = bot.gson.fromJson(text, EuphoriaPacket.class);
            if (packet.type != null) {
                onPacket(packet);
            }
        }catch (JsonSyntaxException e){
            e.printStackTrace();
            System.err.println("Received malformed packet "+text);
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
                setNickname(bot.defaultNickname);
                hello_event event = bot.gson.fromJson(packet.data, hello_event.class);
                sessionView=event.session;
                bot.onJoinRoom(this);
            }
            case SEND_EVENT -> {
                Message message = bot.gson.fromJson(packet.data, Message.class);
                if(!message.sender.id.equals(sessionView.id)){
                    bot.onMessage(message, this);
                }
            }
            default -> {
//                System.out.println("Unimplemented/unrecognized packet: " + bot.gson.toJson(packet)+" with type "+PacketType.fromName(packet.type));
            }
        }


    }
    public void setNickname(String name){
        sendPacket(new EuphoriaPacket(PacketType.NICK,new nick(name)));
    }
    void sendPacket(EuphoriaPacket packet){
        send(bot.gson.toJson(packet));
    }

    CompletableFuture<EuphoriaPacket> sendCommand(PacketType type, Object data){
        //todo check if command type
        EuphoriaPacket packet = new EuphoriaPacket(Snowflake.random().toString(),type,data);
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
    public void onMessage(ByteBuffer message) {}

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }
}