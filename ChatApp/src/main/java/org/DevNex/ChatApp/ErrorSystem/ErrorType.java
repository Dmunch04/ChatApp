package org.DevNex.ChatApp.ErrorSystem;

public enum ErrorType
{

    // HTTP Status Codes: https://httpstatuses.com/
    
    UserNotFound ("UserNotFound", "404"),
    RoomNotFound ("RoomNotFound", "404"),
    MessageNotFound ("MessageNotFound", "404"),

    RoomAlreadyExists ("RoomAlreadyExists", "208"),
    UserAlreadyExists ("UserAlreadyExists", "208"),

    Unknown ("Unknown", "500"),
    InvalidSession ("InvalidSession", "401"),
    NoUserPermission ("NoUserPermission", "403"),

    LoginFail ("LoginFail", "401"),
    RegisterFail ("RegisterFail", "401");

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
