package euphoria;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import euphoria.types.Message;
import euphoria.types.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * Main bot class, override to add your own custom functionality
 */
public class EuphoriaBot {
    public final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Snowflake.class,new Snowflake.SnowflakeAdapter())
            .create();

    /**
     * Map of websocket endpoints to connections
     */
    public final Map<String,RoomConnection> connections = new HashMap<>();
    String defaultNickname;

    private final Logger log = LoggerFactory.getLogger(EuphoriaBot.class);

    //todo figure out this logging stuff

    public EuphoriaBot(String defaultNickname){
        this.defaultNickname=defaultNickname;
    }

    /**
     * Disconnects and removes all sessions
     */
    public final void stop(){
        disconnect();
        connections.clear();
    }

    /**
     * Disconnects from all sessions
     */
    public final void disconnect(){
        connections.values().forEach(connection -> {
            connection.close(1000, "Bot disconnected");
        });
    }
    public final void setNickname(String name,RoomConnection roomConnection){
        roomConnection.setNickname(name);
    }

    public final void joinRoom(URI serverURI){
        if(connections.containsKey(serverURI.toString())) {
            if(!connections.get(serverURI.toString()).isOpen()){
                connections.get(serverURI.toString()).connect();
            }
        }else{
            RoomConnection connection = new RoomConnection(serverURI, this);
            connection.connect();
            connections.put(serverURI.toString(), connection);
        }
    }

    public void onJoinRoom(RoomConnection connection){}
    public void onMessage(Message message,RoomConnection connection){}
}
