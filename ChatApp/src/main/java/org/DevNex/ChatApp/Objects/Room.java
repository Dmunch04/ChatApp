package org.DevNex.ChatApp.Objects;

import java.util.*;

public class Room
{

    private UUID ID;
    private String Display;
    private UUID Creator;
    private String Icon;
    private List<UUID> Clients;
    private Map<UUID, Message> Messages;

    public Room (UUID ID, String Display, UUID Creator, String Icon, List<UUID> Clients, Map<UUID, Message> Messages)
    {
        this.ID = ID;
        this.Display = Display;
        this.Creator = Creator;
        this.Icon = Icon;
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
        Objects.put ("Messages", new ArrayList<Message> (Messages.values ()));

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

    public String GetIcon ()
    {
        return Icon;
    }

    public void SetIcon (String Icon)
    {
        this.Icon = Icon;
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

    public boolean HasClient (UUID ID)
    {
        return Clients.contains (ID);
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

    public Message GetMessage (UUID MessageID)
    {
        if (Messages.containsKey (MessageID))
        {
            return Messages.get (MessageID);
        }

        return null;
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
