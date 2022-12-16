package euphoria;

import java.net.URI;
import java.nio.ByteBuffer;

import com.google.gson.JsonSyntaxException;
import euphoria.packet_types.nick;
import euphoria.packet_types.ping_reply;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class RoomConnection extends WebSocketClient {


    EuphoriaBot bot;
    boolean loggedIn=false;


    public RoomConnection(URI serverUri, EuphoriaBot bot) {
        super(serverUri);
        this.bot=bot;

    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        send("test");
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
            System.out.println("Received malformed packet "+message);
        }
    }
    public void onPacket(EuphoriaPacket packet){
        switch (packet.type.toLowerCase()) {
            case "ping-event" ->
                    sendPacket(new EuphoriaPacket("ping-reply", new ping_reply(packet.data.get("time").getAsLong())));
            case "hello-event" -> {
                loggedIn = true;
                bot.onJoinRoom(this);
            }
            case "send-event" -> bot.onMessage(packet, this);
            default -> System.out.println("Unrecognized packet: " + bot.gson.toJson(packet));
        }


    }
    public void setName(String name){
        sendPacket(new EuphoriaPacket("nick",new nick(name)));
    }
    public void sendPacket(EuphoriaPacket packet){
        System.out.println("sent packet "+bot.gson.toJson(packet));
        send(bot.gson.toJson(packet));
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