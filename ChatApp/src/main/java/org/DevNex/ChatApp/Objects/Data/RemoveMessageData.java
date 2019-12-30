package org.DevNex.ChatApp.Objects.Data;

import java.util.Map;
import java.util.UUID;

public class RemoveMessageData
{

    private String Token;

    private UUID UserID;
    private UUID RoomID;
    private UUID MessageID;

    public RemoveMessageData (Map<String, String> Args)
    {
        this (Args.get ("Token"), Args.get ("UserID"), Args.get ("RoomID"), Args.get ("MessageID"));
    }

    public RemoveMessageData (String Token, String UserID, String RoomID, String MessageID)
    {
        this.Token = Token;

        this.UserID = UUID.fromString (UserID);
        this.RoomID = UUID.fromString (RoomID);
        this.MessageID = UUID.fromString (MessageID);
    }

    public String GetToken ()
    {
        return Token;
    }

    public UUID GetUserID ()
    {
        return UserID;
    }

    public UUID GetRoomID ()
    {
        return RoomID;
    }

    public UUID GetMessageID ()
    {
        return MessageID;
    }

}
