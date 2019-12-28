package org.TeamName.ChatApp.Server;

public enum SocketIOEvents
{

    LOGIN ("login"),
    REGISTER ("register"),
    REMOVE_USER ("remove-user"),

    CREATE_ROOM ("create-room"),
    JOIN_ROOM ("join-room"),
    REMOVE_ROOM ("remove-room"),

    SEND_MESSAGE ("send-message"),
    REMOVE_MESSAGE ("remove-message"),
    KICK_USER ("kick-user");

    private String EventName;

    private SocketIOEvents (String EventName)
    {
        this.EventName = EventName;
    }

    public String GetEventName ()
    {
        return EventName;
    }

}
