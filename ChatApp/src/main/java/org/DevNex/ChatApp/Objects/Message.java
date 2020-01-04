package org.DevNex.ChatApp.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Message
{

    private UUID ID;
    private UUID RoomID;
    private UUID SenderID;
    private String Content;

    public Message (UUID ID, UUID RoomID, UUID SenderID, String Content)
    {
        this.ID = ID;
        this.RoomID = RoomID;
        this.SenderID = SenderID;
        this.Content = Content;
    }

    public Map<String, String> ToMap ()
    {
        Map<String, String> Objects = new HashMap<String, String> ();
        Objects.put ("ID", ID.toString ());
        Objects.put ("SenderID", SenderID.toString ());
        Objects.put ("RoomID", RoomID.toString ());
        Objects.put ("Content", Content);

        return Objects;
    }

    public UUID GetID ()
    {
        return ID;
    }

    public UUID GetRoomID ()
    {
        return RoomID;
    }

    public UUID GetSenderID ()
    {
        return SenderID;
    }

    public String GetContent ()
    {
        return Content;
    }

}
