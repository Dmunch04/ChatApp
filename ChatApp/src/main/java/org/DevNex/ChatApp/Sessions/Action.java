package org.DevNex.ChatApp.Sessions;

import org.DevNex.ChatApp.Utils.Helper;

import java.sql.Timestamp;

public class Action
{

    public ActionType Type;
    public String Description;
    public Timestamp Timestamp;

    public Action (String Type, String Description)
    {
        this (ActionType.valueOf (Type), Description);
    }

    public Action (String Type)
    {
        this (ActionType.valueOf (Type), "");
    }

    public Action (ActionType Type)
    {
        this (Type, "");
    }

    public Action (ActionType Type, String Description)
    {
        this.Type = Type;
        this.Description = Description;
        this.Timestamp = Helper.GetTimestamp ();
    }

}
