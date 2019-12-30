package org.DevNex.ChatApp.Server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import org.DevNex.ChatApp.Objects.Data.*;
import org.DevNex.ChatApp.Sessions.ActionType;
import org.DevNex.ChatApp.Sessions.Session;
import org.DevNex.ChatApp.Sessions.SessionTracker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/*
    SocketIOHandler:
    This class handles all the traffic of the Socket.io server. It receives requests from a client, processes
    the data and sends response back
*/
public class SocketIOHandler
{

    private Server Instance;
    private SocketIOServer Server;
    private SessionTracker Tracker;

    public SocketIOHandler (Server Instance, SocketIOServer Server)
    {
        this.Instance = Instance;
        this.Server = Server;
        this.Tracker = Instance.GetSessionTracker ();

        Listen ();
    }

    /*
        Send event + data to client:
        Client.sendEvent ("event", DataClass);
    */
    private void Listen ()
    {
        Server.addConnectListener (new ConnectListener () {
            @Override
            public void onConnect (SocketIOClient Client)
            {
                System.out.println ("Client connected: " + Client.getSessionId ().toString ());
            }
        });

        Server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect (SocketIOClient Client)
            {
                if (Tracker.GetSession (Client.getSessionId ()) != null)
                {
                    Tracker.GetSession (Client.getSessionId ()).AddAction (ActionType.LOGOUT);
                    Tracker.RemoveSessionBySessionID (Client.getSessionId ());
                }

                System.out.println ("Client disconnected: " + Client.getSessionId ().toString ());
            }
        });

        Server.addEventListener (SocketIOEvents.LOGIN.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                LoginRegisterData Data = (LoginRegisterData) CreateClass (LoginRegisterData.class, Args);

                // TODO: Login

                // TODO: Replace the `""` with the users ID, obtained by getting from DB using the Token
                // TODO: Fix `Invalid UUID string` error
                Tracker.AddSession (new Session (Data.GetToken (), Client.getSessionId (), ""));
            }
        });

        Server.addEventListener (SocketIOEvents.REGISTER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                LoginRegisterData Data = (LoginRegisterData) CreateClass (LoginRegisterData.class, Args);

                // TODO: Register

                // TODO: Replace the `""` with the users ID, obtained by getting from DB using the Token
                // TODO: Fix `Invalid UUID string` error
                Tracker.AddSession (new Session (Data.GetToken (), Client.getSessionId (), ""));
            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_USER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                RemoveUserData Data = (RemoveUserData) CreateClass (RemoveUserData.class, Args);
            }
        });

        Server.addEventListener (SocketIOEvents.CREATE_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                CreateRoomData Data = (CreateRoomData) CreateClass (CreateRoomData.class, Args);
            }
        });

        Server.addEventListener (SocketIOEvents.JOIN_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                JoinRemoveRoomData Data = (JoinRemoveRoomData) CreateClass (JoinRemoveRoomData.class, Args);
            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                JoinRemoveRoomData Data = (JoinRemoveRoomData) CreateClass (JoinRemoveRoomData.class, Args);
            }
        });

        Server.addEventListener (SocketIOEvents.SEND_MESSAGE.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                SendMessageData Data = (SendMessageData) CreateClass (SendMessageData.class, Args);
            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_MESSAGE.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                RemoveMessageData Data = (RemoveMessageData) CreateClass (RemoveMessageData.class, Args);
            }
        });

        Server.addEventListener (SocketIOEvents.KICK_USER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                KickUserData Data = (KickUserData) CreateClass (KickUserData.class, Args);
            }
        });
    }

    public Object CreateClass (Class<?> Target, Map<String, String> Args)
    {
        try
        {
            Constructor<?> TargetConstructor =  Target.getConstructor (Map.class);
            Object ClassObject = TargetConstructor.newInstance (Args);
            return ClassObject;
        }

        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException Error)
        {
            Error.printStackTrace ();
        }

        return null;
    }

}
