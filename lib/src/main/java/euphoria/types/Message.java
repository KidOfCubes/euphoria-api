package euphoria.types;

public class Message {
    /**
     * Snowflake of parent message
     */
    public Snowflake parent;
    public String content;

    /**
     * Snowflake of this message
     */
    public Snowflake id;
    public Snowflake previous_edit_id;
    public long time;
    public SessionView sender; //SessionView, implement l8r
    public String encryption_key_id;
    public long edited;
    public long deleted;
    public boolean truncated;

    //maybe add sender


    public Message(String content){
        this.content=content;
    }
    public Message(String content,Snowflake parent){
        this.content=content;
        this.parent=parent;
    }
}
