package org.DevNex.ChatApp.Objects;

import java.util.HashMap;
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

    public Map<String, Object> ToMap ()
    {
        Map<String, Object> Objects = new HashMap<String, Object> ();
        Objects.put ("ID", ID.toString ());
        Objects.put ("Display", Display);
        Objects.put ("Creator", Creator.toString ());
        Objects.put ("Clients", GetClientsString ());
        Objects.put ("Messages", Messages.values ());

        return Objects;
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

    public String GetClientsString ()
    {
        if (Clients.isEmpty ()) return ".";

        StringBuilder ClientsString = new StringBuilder ();

        for (UUID Client : Clients)
        {
            ClientsString.append (Client.toString ()).append (",");
        }

        return ClientsString.substring (0, ClientsString.toString ().length () - 1);
    }

    public Map<UUID, Message> GetMessages ()
    {
        return Messages;
    }

    public void AddMessage (UUID ID, Message Object)
    {
        Messages.put (ID, Object);
    }

    public void RemoveMessage (UUID ID)
    {
        Messages.remove (ID);
    }

}
