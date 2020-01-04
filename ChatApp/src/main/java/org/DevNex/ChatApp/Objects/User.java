package org.DevNex.ChatApp.Objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class User
{

    private String Token;
    private UUID ID;
    private String Username;
    private String Password;
    private List<UUID> Rooms;

    public User (String Token, UUID ID, String Username, String Password, List<UUID> Rooms)
    {
        this.Token = Token;
        this.ID = ID;
        this.Username = Username;
        this.Password = Password;
        this.Rooms = Rooms;
    }

    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Objects = new HashMap<String, Object>();
        Objects.put ("Token", Token);
        Objects.put ("ID", ID.toString ());
        Objects.put ("Username", Username);
        Objects.put ("Rooms", GetRoomsString ());

        return Objects;
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

    public String GetPassword ()
    {
        return Password;
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

    public String GetRoomsString ()
    {
        if (Rooms.isEmpty ()) return ".";

        StringBuilder RoomsString = new StringBuilder ();

        for (UUID Room : Rooms)
        {
            RoomsString.append (Room.toString ()).append (",");
        }

        return RoomsString.substring (0, Rooms.size () - 1);
    }

}
