package org.TeamName.ChatApp.Objects;

import java.util.List;
import java.util.UUID;

public class Room
{

    private UUID ID;
    private String Display;
    private UUID Creator;
    private List<UUID> Clients;

    public Room (UUID ID, String Display, UUID Creator, List<UUID> Clients)
    {
        this.ID = ID;
        this.Display = Display;
        this.Creator = Creator;
        this.Clients = Clients;
    }

    public UUID GetID ()
    {
        return ID;
    }

    public String GetDisplay ()
    {
        return Display;
    }

    public UUID GetCreator ()
    {
        return Creator;
    }

    public List<UUID> GetClients ()
    {
        return Clients;
    }

    public void AddClient (UUID ID)
    {
        if (!Clients.contains (ID))
        {
            Clients.add (ID);
        }
    }

    public void RemoveClient (UUID ID)
    {
        if (Clients.contains (ID))
        {
            Clients.remove (ID);
        }
    }

}
