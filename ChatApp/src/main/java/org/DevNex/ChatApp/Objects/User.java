package org.DevNex.ChatApp.Objects;

import java.util.List;
import java.util.UUID;

public class User
{

    private String Token;
    private UUID ID;
    private String Username;
    private List<UUID> Rooms;

    public User (String Token, UUID ID, String Username, List<UUID> Rooms)
    {
        this.Token = Token;
        this.ID = ID;
        this.Username = Username;
        this.Rooms = Rooms;
    }

    public String GetToken ()
    {
        return Token;
    }

    public UUID GetID ()
    {
        return ID;
    }

    public String GetUsername ()
    {
        return Username;
    }

    public List<UUID> GetRooms ()
    {
        return Rooms;
    }

    public void AddRoom (UUID ID)
    {
        if (!Rooms.contains (ID))
        {
            Rooms.add (ID);
        }
    }

    public void RemoveRoom (UUID ID)
    {
        if (Rooms.contains (ID))
        {
            Rooms.remove (ID);
        }
    }

}
