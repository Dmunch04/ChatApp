package org.DevNex.ChatApp.Objects.Data;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class LoginRegisterData
{

    private String Token;

    private String Username;
    private String Password;
    private Timestamp Timestamp;

    public LoginRegisterData () { System.out.println ("0"); }

    public LoginRegisterData (String aa) { System.out.println (aa); }

    public LoginRegisterData (Map<String, String> aa) { System.out.println ("1"); }

    public LoginRegisterData (JSONPObject aa) { System.out.println ("2"); }

    public LoginRegisterData (List<String> aa) { System.out.println ("3"); }

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
