package org.DevNex.ChatApp.Objects;

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
