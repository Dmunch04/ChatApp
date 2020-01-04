package org.DevNex.ChatApp.Objects.Data;

import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;
import java.util.Map;

public class LoginRegisterData
{

    private String Username;
    private String Password;
    private Timestamp Timestamp;

    public LoginRegisterData (Map<String, String> Args)
    {
        this (Args.get ("Username"), Args.get ("Password"));
    }

    public LoginRegisterData (String Username, String Password)
    {
        super ();

        this.Username = Username;
        this.Password = Password;
        this.Timestamp = Helper.GetTimestamp ();
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
