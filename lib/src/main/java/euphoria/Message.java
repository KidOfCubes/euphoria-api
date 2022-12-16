package euphoria;

public class Message {
    String parentSnowflake;
    String content;

    String id;
    String previous_edit_id;
    long time;
    Object sender; //SessionView, implement l8r
    String encryption_key_id;
    long edited;
    long deleted;
    boolean truncated;
    public Message(String content){
        this.content=content;
    }
    public Message(String content,String parent){
        this.content=content;
        this.parentSnowflake=parent;
    }
}
