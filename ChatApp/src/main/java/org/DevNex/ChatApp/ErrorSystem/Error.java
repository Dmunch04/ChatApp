package org.DevNex.ChatApp.ErrorSystem;

import java.util.HashMap;
import java.util.Map;

public class Error
{

    private String Type;
    private String Code;
    private String Error;

    public Error (ErrorType Type, String Error)
    {
        this (Type.GetName (), Type.GetCode (), Error);
    }

    public Error (String Type, String Code, String Error)
    {
        this.Type = Type;
        this.Code = Code;
        this.Error = Error;
    }

    public Map<String, String> ToMap ()
    {
        Map<String, String> Objects = new HashMap<String, String> ();
        Objects.put ("Type", Type);
        Objects.put ("Code", Code);
        Objects.put ("Error", Error);

        return Objects;
    }

}
