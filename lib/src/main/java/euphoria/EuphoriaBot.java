package euphoria;

import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EuphoriaBot {
    public final Gson gson = new Gson();

    public String name;
    public EuphoriaBot(String name){
        this.name=name;
    }
    private final List<RoomConnection> connections = new ArrayList<>();

    public void joinRoom(URI serverURI){
        RoomConnection connection = new RoomConnection(serverURI,this);
        connection.connect();
        connections.add(connection);
    }
    public void broadcast(String text){
        Message message = new Message(text);
        connections.forEach(connection -> connection.sendPacket(new EuphoriaPacket("send",message)));
    }
    public void onJoinRoom(RoomConnection connection){
        connection.setName("BotLibraryTest");
        broadcast("test");

    }
    public void onMessage(EuphoriaPacket packet,RoomConnection connection){
        System.out.println("message "+gson.toJson(packet));
    }
}
