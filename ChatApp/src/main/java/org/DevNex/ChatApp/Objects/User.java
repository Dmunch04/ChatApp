package org.DevNex.ChatApp.Objects;

import java.util.List;
import java.util.UUID;

public class User
{

    private UUID ID;
    private String Username;
    private List<UUID> Rooms;

    public User (UUID ID, String Username, List<UUID> Rooms)
    {
        this.ID = ID;
        this.Username = Username;
        this.Rooms = Rooms;
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
