package org.DevNex.ChatApp.Objects;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Room
{

    private UUID ID;
    private String Display;
    private UUID Creator;
    private List<UUID> Clients;
    private Map<UUID, Message> Messages;

    public Room (UUID ID, String Display, UUID Creator, List<UUID> Clients, Map<UUID, Message> Messages)
    {
        this.ID = ID;
        this.Display = Display;
        this.Creator = Creator;
        this.Clients = Clients;
        this.Messages = Messages;
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

    public Map<UUID, Message> GetMessages ()
    {
        return Messages;
    }
}
