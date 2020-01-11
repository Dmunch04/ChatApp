package org.DevNex.ChatApp.Objects.Data;

import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

public class StringData
{

    private String Token;

    private UUID UserID;
    private String Data;

    public StringData (Map<String, String> Args)
    {
        this (Args.get ("Token"), Args.get ("UserID"), Args.get ("Data"));
    }

    public StringData (String Token, String UserID, String Data)
    {
        this.Token = Token;

        this.UserID = UUID.fromString (UserID);
        this.Data = Data;
    }

    public String GetToken ()
    {
        return Token;
    }

    public UUID GetUserID ()
    {
        return UserID;
    }

    public String GetData ()
    {
        return Data;
    }

}
