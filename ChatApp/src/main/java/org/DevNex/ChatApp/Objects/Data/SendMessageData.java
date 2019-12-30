package org.DevNex.ChatApp.Objects.Data;

import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

public class SendMessageData
{

    private String Token;

    private UUID UserID;
    private UUID RoomID;
    private String Message;
    private Timestamp Timestamp;

    public SendMessageData (Map<String, String> Args)
    {
        this (Args.get ("Token"), Args.get ("UserID"), Args.get ("RoomID"), Args.get ("Message"));
    }

    public SendMessageData (String Token, String UserID, String RoomID, String Message)
    {
        this.Token = Token;

        this.UserID = UUID.fromString (UserID);
        this.RoomID = UUID.fromString (RoomID);
        this.Message = Message;
        this.Timestamp = Helper.GetTimestamp ();
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

    public String GetMessage ()
    {
        return Message;
    }

    public Timestamp GetTimestamp ()
    {
        return Timestamp;
    }

}
