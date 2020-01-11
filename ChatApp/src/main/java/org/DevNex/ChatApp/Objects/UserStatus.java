package org.DevNex.ChatApp.Objects;

public enum UserStatus
{

    OFFLINE ("Offline"),
    ONLINE ("Online"),
    AWAY ("Away"),
    DO_NOT_DISTURB ("Do Not Disturb");

    private String Status;

    private UserStatus (String Status)
    {
        this.Status = Status;
    }

    public String GetStatusName ()
    {
        return Status;
    }

}
