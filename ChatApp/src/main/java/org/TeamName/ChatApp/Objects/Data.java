package org.TeamName.ChatApp.Objects;

import java.util.UUID;

public class Data
{

    private UUID ID;
    private String Type;
    private Object Content;

    public Data () { }

    public Data (UUID ID)
    {
        super ();

        this.ID = ID;
    }

    public Data (UUID ID, String Type)
    {
        super ();

        this.ID = ID;
        this.Type = Type;
    }

    public Data (UUID ID, String Type, Object Content)
    {
        super ();

        this.ID = ID;
        this.Type = Type;
        this.Content = Content;
    }

    public UUID GetID ()
    {
        return ID;
    }

}
