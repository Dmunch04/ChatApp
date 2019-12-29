package org.DevNex.ChatApp.Objects.Data;

import java.util.UUID;

public class RemoveUserData
{

    private String Token;

    private UUID UserID;
    private UUID RoomID;

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
