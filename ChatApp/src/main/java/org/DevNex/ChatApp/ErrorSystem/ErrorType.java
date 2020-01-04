package org.DevNex.ChatApp.ErrorSystem;

public enum ErrorType
{

    UserNotFound ("UserNotFound", "404"),
    RoomNotFound ("RoomNotFound", "404"),
    MessageNotFound ("MessageNotFound", "404"),

    RoomAlreadyExists ("RoomAlreadyExists", "000"),
    UserAlreadyExists ("UserAlreadyExists", "000"),

    Unknown ("Unknown", "000"),

    LoginFail ("LoginFail", "000"),
    RegisterFail ("RegisterFail", "000");

    private String Name;
    private String Code;

    private ErrorType (String Name, String Code)
    {
        this.Name = Name;
        this.Code = Code;
    }

    public String GetName ()
    {
        return Name;
    }

    public String GetCode ()
    {
        return Code;
    }

}
