package org.DevNex.ChatApp.Objects.Data;

import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;

public class LoginRegisterData
{

    private String Token;

    private String Username;
    private String Password;
    private Timestamp Timestamp;

    public LoginRegisterData () { }

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
