package org.DevNex.ChatApp.Objects.Data;

import java.util.Map;
import java.util.UUID;

public class RemoveUserData
{

    private String Token;

    private UUID UserID;
    private UUID RoomID;

    public RemoveUserData (Map<String, String> Args)
    {
        this (Args.get ("Token"), Args.get ("UserID"), Args.get ("RoomID"), Args.get ("TargetID"));
    }

    public RemoveUserData (String Token, String UserID, String RoomID, String TargetID)
    {
        this.Token = Token;

        this.UserID = UUID.fromString (UserID);
        this.RoomID = UUID.fromString (RoomID);
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

}
