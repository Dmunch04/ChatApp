package org.DevNex.ChatApp.Objects;

import java.util.UUID;

public class Data
{

    private UUID ID;
    private String Type;
    private Object Content;

    public Data () { }

    public Data (String ID)
    {
        super ();

        System.out.println (ID);

        this.ID = UUID.fromString (ID);
    }

    public Data (String ID, String Type)
    {
        super ();

        this.ID = UUID.fromString (ID);
        this.Type = Type;
    }

    public Data (String ID, String Type, Object Content)
    {
        super ();

        this.ID = UUID.fromString (ID);
        this.Type = Type;
        this.Content = Content;
    }

    public UUID GetID ()
    {
        return ID;
    }
    public String GetType ()
    {
        return Type;
    }
    public Object GetContent ()
    {
        return Content;
    }

}
