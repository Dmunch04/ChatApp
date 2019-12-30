package org.DevNex.ChatApp.Objects.Data;

import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;
import java.util.Map;

public class LoginRegisterData
{

    private String Token;

    private String Username;
    private String Password;
    private Timestamp Timestamp;

    public LoginRegisterData (Map<String, String> Args)
    {
        this (Args.get ("Token"), Args.get ("Username"), Args.get ("Passwords"));
    }

    public LoginRegisterData (String Token, String Username, String Password)
    {
        super ();

        this.Token = Token;

        this.Username = Username;
        this.Password = Password;
        this.Timestamp = Helper.GetTimestamp ();
    }

    public String GetToken ()
    {
        return Token;
    }

    public String GetUsername ()
    {
        return Username;
    }

    public String GetPassword ()
    {
        return Password;
    }

    public Timestamp GetTimestamp ()
    {
        return Timestamp;
    }

}
