package org.DevNex.ChatApp.Objects.Data;

import java.util.Map;
import java.util.UUID;

public class CreateRoomData
{

    private String Token;

    private String Display;
    private UUID UserID;

    public CreateRoomData (Map<String, String> Args)
    {
        this (Args.get ("Token"), Args.get ("Display"), Args.get ("UserID"));
    }

    public CreateRoomData (String Token, String Display, String UserID)
    {
        this.Token = Token;

        this.Display = Display;
        this.UserID = UUID.fromString (UserID);
    }

    public String GetToken ()
    {
        return Token;
    }

    public String GetDisplay ()
    {
        return Display;
    }

    public UUID GetUserID ()
    {
        return UserID;
    }

}
