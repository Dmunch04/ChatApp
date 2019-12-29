package org.DevNex.ChatApp.Objects.Data;

import java.util.UUID;

public class KickUserData
{

    private String Token;

    private UUID UserID;
    private UUID RoomID;
    private UUID TargetID;

    public KickUserData (String Token, String UserID, String RoomID, String TargetID)
    {
        this.Token = Token;

        this.UserID = UUID.fromString (UserID);
        this.RoomID = UUID.fromString (RoomID);
        this.TargetID = UUID.fromString (TargetID);
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

    public UUID GetTargetID ()
    {
        return TargetID;
    }

}
