package euphoria.packet_types;

import euphoria.types.PersonalAccountView;
import euphoria.types.SessionView;

public class hello_event {
    public String id; //todo implement userid
    public PersonalAccountView account;
    public SessionView session;
    public boolean account_has_access;
    public boolean account_email_verified;
    public boolean room_is_private;
    public String version;
}
