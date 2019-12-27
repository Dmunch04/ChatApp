package org.TeamName.ChatApp.Objects;

import java.util.UUID;

public class Message
{

    private UUID ID;
    private UUID RoomID;
    private UUID SenderID;

    public Message (UUID ID, UUID RoomID, UUID SenderID)
    {
        this.ID = ID;
        this.RoomID = RoomID;
        this.SenderID = SenderID;
    }

    public UUID GetID ()
    {
        return ID;
    }

    public UUID GetRoomID ()
    {
        return RoomID;
    }

    public UUID SenderID ()
    {
        return SenderID;
    }

}
