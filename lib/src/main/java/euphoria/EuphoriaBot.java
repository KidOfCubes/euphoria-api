package euphoria;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import euphoria.types.Message;
import euphoria.types.PacketType;
import euphoria.types.Snowflake;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Main bot class, override to add your own custom functionality
 */
public class EuphoriaBot {
    public final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Snowflake.class,new Snowflake.SnowflakeAdapter())
            .create();
    protected final Map<String,RoomConnection> connections = new HashMap<>();
    public String name;
    public EuphoriaBot(String name){
        this.name=name;
    }
    public void setName(String name){
        connections.values().forEach(connection -> setName(name,connection));
    }
    public void setName(String name,RoomConnection roomConnection){
        roomConnection.setName(name);
    }

    public void joinRoom(URI serverURI){
        RoomConnection connection = new RoomConnection(serverURI,this);
        connection.connect();
        connections.put(serverURI.toString(), connection);
    }
    public void broadcast(String text){
        Message message = new Message(text);
        connections.values().forEach(connection -> connection.sendPacket(new EuphoriaPacket(PacketType.SEND,message)));
    }
    public void onJoinRoom(RoomConnection connection){}
    public void onMessage(Message message,RoomConnection connection){}
}
