package euphoria;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EuphoriaPacket {
    private static final Gson encoder = new Gson();
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

    public EuphoriaPacket(String type, Object data){
        this.type=type;
        this.data=encoder.toJsonTree(data).getAsJsonObject();
    }
}
