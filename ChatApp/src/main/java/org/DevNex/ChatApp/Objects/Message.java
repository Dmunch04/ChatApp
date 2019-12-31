package org.DevNex.ChatApp.Objects;

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

    public String GetContent ()
    {
        return Content;
    }

}
