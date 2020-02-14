package org.DevNex.ChatApp.Sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionTracker
{

    private List<Session> Sessions;

    public SessionTracker ()
    {
        this.Sessions = new ArrayList<Session> ();
    }

    public List<Session> GetSessions ()
    {
        return Sessions;
    }

    public boolean HasSession (UUID ID)
    {
        for (Session Session : Sessions)
        {
            if (Session.GetID ().equals (ID)) return true;
        }

        return false;
    }

    public Session GetSession (UUID SessionID)
    {
        for (Session Value : Sessions)
        {
            if (Value.GetSessionID ().equals (SessionID)) return Value;
        }

        return null;
    }

    public Session GetSessionByID (UUID ID)
    {
        for (Session Value : Sessions)
        {
            if (Value.GetID ().equals (ID)) return Value;
        }

        return null;
    }

    public Session GetSessionByToken (String Token)
    {
        for (Session Value : Sessions)
        {
            if (Value.GetToken ().equals (Token)) return Value;
        }

        return null;
    }

    public void AddSession (Session Value)
    {
        this.Sessions.add (Value);
    }

    public void RemoveSession (int Index)
    {
        this.Sessions.remove (Index);
    }

    public void RemoveSession (Session Value)
    {
        this.Sessions.remove (Value);
    }

    public void RemoveSessionByToken (String Token)
    {
        this.Sessions.removeIf (Value -> Value.GetToken ().equals (Token));
    }

    public void RemoveSessionBySessionID (UUID SessionID)
    {
        this.Sessions.removeIf (Value -> Value.GetSessionID ().equals (SessionID));
    }

    public void RemoveSessionByID (UUID ID)
    {
        this.Sessions.removeIf (Value -> Value.GetID ().equals (ID));
    }

}
