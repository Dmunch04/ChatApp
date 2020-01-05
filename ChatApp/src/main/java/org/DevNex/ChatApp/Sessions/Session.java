package org.DevNex.ChatApp.Sessions;

import org.DevNex.ChatApp.Database.DatabaseHelper;
import org.DevNex.ChatApp.Objects.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session
{

    private String Token;
    private UUID SessionID;
    private UUID ID;
    private List<Action> Actions;

    public Session (String Token, UUID SessionID, String ID)
    {
        this (Token, SessionID, UUID.fromString (ID));
    }

    public Session (String Token, UUID SessionID, UUID ID)
    {
        this.Token = Token;
        this.SessionID = SessionID;
        this.ID = ID;
        this.Actions = new ArrayList<Action> ();

        Actions.add (new Action (ActionType.LOGIN));
    }

    public String GetToken ()
    {
        return Token;
    }

    public UUID GetSessionID ()
    {
        return SessionID;
    }

    public UUID GetID ()
    {
        return ID;
    }

    public User GetUser ()
    {
        // TODO: Get user object from DB using `this.ID`
        // Hmm. We need to find a better way, since right now each session instance would then need an `DatabaseHelper` instance
        throw new NotImplementedException ();
    }

    public List<Action> GetActions ()
    {
        return Actions;
    }

    public Action GetAction (int Index)
    {
        return Actions.get (Index);
    }

    public List<Action> GetActionByType (ActionType Type)
    {
        List ReturnActions =  new ArrayList<Action> ();

        for (Action Value : Actions)
        {
            if (Value.Type.equals (Type)) ReturnActions.add (Value);
        }

        return ReturnActions;
    }

    public void AddAction (Action Value)
    {
        this.Actions.add (Value);
    }

    public void AddAction (ActionType Type)
    {
        this.Actions.add (new Action (Type));
    }

    public void RemoveAction (int Index)
    {
        this.Actions.remove (Index);
    }

    public void RemoveAction (Action Value)
    {
        this.Actions.remove (Value);
    }

    public void RemoveActionByType (ActionType Type)
    {
        this.Actions.removeIf (Value -> Value.Type.equals (Type));
    }

}
