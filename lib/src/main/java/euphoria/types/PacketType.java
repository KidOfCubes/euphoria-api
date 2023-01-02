package euphoria.types;

import java.util.HashMap;
import java.util.Map;

//todo fully implement in EuphoriaPacket
public enum PacketType {
    
    BOUNCE_EVENT("bounce-event"),
    DISCONNECT_EVENT("disconnect-event"),
    EDIT_MESSAGE_EVENT("edit-message-event"),
    HELLO_EVENT("hello-event"),
    JOIN_EVENT("join-event"),
    LOGIN_EVENT("login-event"),
    LOGOUT_EVENT("logout-event"),
    NETWORK_EVENT("network-event"),
    NICK_EVENT("nick-event"),
    PART_EVENT("part-event"),
    PING_EVENT("ping-event"),
    PM_INITIATE_EVENT("pm-initiate-event"),
    SEND_EVENT("send-event"),


    AUTH("auth"),
    PING("ping"),
    GET_MESSAGE("get-message"),
    LOG("log"),
    NICK("nick"),
    PM_INITIATE("pm-initiate"),
    SEND("send"),
    WHO("who"),
    CHANGE_EMAIL("change-email"),
    CHANGE_NAME("change-name"),
    CHANGE_PASSWORD("change-password"),
    LOGIN("login"),
    LOGOUT("logout"),
    REGISTER_ACCOUNT("register-account"),
    RESEND_VERIFICATION_EMAIL("resend-verification-email"),
    RESET_PASSWORD("reset-password"),
    BAN("ban"),
    EDIT_MESSAGE("edit-message"),
    GRANT_ACCESS("grant-access"),
    GRANT_MANAGER("grant-manager"),
    REVOKE_ACCESS("revoke-access"),
    REVOKE_MANAGER("revoke-manager"),
    UNBAN("unban"),
    STAFF_CREATE_ROOM("staff-create-room"),
    STAFF_GRANT_MANAGER("staff-grant-manager"),
    STAFF_ENROLL_OTP("staff-enroll-otp"),
    STAFF_INVADE("staff-invade"),
    STAFF_LOCK_ROOM("staff-lock-room"),
    STAFF_REVOKE_ACCESS("staff-revoke-access"),
    STAFF_REVOKE_MANAGER("staff-revoke-manager"),
    STAFF_VALIDATE_OTP("staff-validate-otp"),
    UNLOCK_STAFF_CAPABILITY("unlock-staff-capability"),

    AUTH_REPLY("auth-reply"),
    PING_REPLY("ping-reply"),
    GET_MESSAGE_REPLY("get-message-reply"),
    LOG_REPLY("log-reply"),
    NICK_REPLY("nick-reply"),
    PM_INITIATE_REPLY("pm-initiate-reply"),
    SEND_REPLY("send-reply"),
    WHO_REPLY("who-reply"),
    CHANGE_EMAIL_REPLY("change-email-reply"),
    CHANGE_NAME_REPLY("change-name-reply"),
    CHANGE_PASSWORD_REPLY("change-password-reply"),
    LOGIN_REPLY("login-reply"),
    LOGOUT_REPLY("logout-reply"),
    REGISTER_ACCOUNT_REPLY("register-account-reply"),
    RESEND_VERIFICATION_EMAIL_REPLY("resend-verification-email-reply"),
    RESET_PASSWORD_REPLY("reset-password-reply"),
    BAN_REPLY("ban-reply"),
    EDIT_MESSAGE_REPLY("edit-message-reply"),
    GRANT_ACCESS_REPLY("grant-access-reply"),
    GRANT_MANAGER_REPLY("grant-manager-reply"),
    REVOKE_ACCESS_REPLY("revoke-access-reply"),
    REVOKE_MANAGER_REPLY("revoke-manager-reply"),
    UNBAN_REPLY("unban-reply"),
    STAFF_CREATE_ROOM_REPLY("staff-create-room-reply"),
    STAFF_GRANT_MANAGER_REPLY("staff-grant-manager-reply"),
    STAFF_ENROLL_OTP_REPLY("staff-enroll-otp-reply"),
    STAFF_INVADE_REPLY("staff-invade-reply"),
    STAFF_LOCK_ROOM_REPLY("staff-lock-room-reply"),
    STAFF_REVOKE_ACCESS_REPLY("staff-revoke-access-reply"),
    STAFF_REVOKE_MANAGER_REPLY("staff-revoke-manager-reply"),
    STAFF_VALIDATE_OTP_REPLY("staff-validate-otp-reply"),
    UNLOCK_STAFF_CAPABILITY_REPLY("unlock-staff-capability-reply"),
    UNKNOWN("unknown");

    private final String name;
    private static final Map<String,PacketType> map = new HashMap<>();

    public static PacketType fromName(String name){
        if(map.size()==0){
            for(int i=0;i<PacketType.values().length;i++){
                map.put(PacketType.values()[i].getName(),PacketType.values()[i]);
            }
        }
        return map.getOrDefault(name,PacketType.UNKNOWN);
    }

    PacketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
