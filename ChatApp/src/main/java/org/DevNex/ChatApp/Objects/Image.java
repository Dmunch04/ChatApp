package org.DevNex.ChatApp.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Image
{

    private UUID ID;
    private UUID SenderID;
    private UUID RoomID;

    public Image (UUID ID, UUID SenderID, UUID RoomID)
    {
        this.ID = ID;
        this.SenderID = SenderID;
        this.RoomID = RoomID;
    }

    public Map<String, String> ToMap ()
    {
        Map<String, String> Objects = new HashMap<String, String> ();
        Objects.put ("ID", ID.toString ());
        Objects.put ("SenderID", SenderID.toString ());
        Objects.put ("RoomID", RoomID.toString ());

        return Objects;
    }

    public UUID GetID ()
    {
        return ID;
    }

    public UUID GetSenderID ()
    {
        return SenderID;
    }

    public UUID GetRoomID ()
    {
        return RoomID;
    }

}
