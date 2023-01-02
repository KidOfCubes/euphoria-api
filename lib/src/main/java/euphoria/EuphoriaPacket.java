package euphoria;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import euphoria.types.PacketType;
import euphoria.types.Snowflake;


public class EuphoriaPacket {
    private static final Gson encoder = new GsonBuilder().registerTypeHierarchyAdapter(Snowflake.class,new Snowflake.SnowflakeAdapter()).create();
    /**
     * optional 	client-generated id for associating replies with commands
     */
    public String id;
    /**
     * required 	the name of the command, reply, or event
     */
    public String type;
    /**
     * optional 	the payload of the command, reply, or event
     */
    public JsonObject data;
    /**
     * optional 	this field appears in replies if a command fails
     */
    public String error;
    /**
     * optional 	this field appears in replies to warn the client that it may be flooding; the client should slow down its command rate
     */
    public boolean throttled;
    /**
     * optional 	if throttled is true, this field describes why
     */
    public String throttled_reason;

    public EuphoriaPacket(PacketType type, Object data){
        this.type=type.getName();
        this.data=encoder.toJsonTree(data).getAsJsonObject();
    }

    public EuphoriaPacket(String id, PacketType type, Object data){
        this.id=id;
        this.type=type.getName();
        this.data=encoder.toJsonTree(data).getAsJsonObject();
    }
}
