package org.DevNex.ChatApp.Server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import org.DevNex.ChatApp.Database.DatabaseHelper;
import org.DevNex.ChatApp.ErrorSystem.Error;
import org.DevNex.ChatApp.ErrorSystem.ErrorType;
import org.DevNex.ChatApp.Objects.Data.*;
import org.DevNex.ChatApp.Objects.User;
import org.DevNex.ChatApp.Sessions.ActionType;
import org.DevNex.ChatApp.Sessions.Session;
import org.DevNex.ChatApp.Sessions.SessionTracker;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.DevNex.ChatApp.Utils.Helper;
import static org.DevNex.ChatApp.Utils.Helper.CreateClass;

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
    private DatabaseHelper DBHelper;

    public SocketIOHandler (Server Instance, SocketIOServer Server)
    {
        this.Instance = Instance;
        this.Server = Server;
        this.Tracker = Instance.GetSessionTracker ();
        this.DBHelper = Instance.GetDatabaseHelper ();

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

                // TODO: Fix `Invalid UUID string` error
                User Target = DBHelper.GetUser (Data.GetUsername ());
                Tracker.AddSession (new Session (Target.GetToken (), Client.getSessionId (), Target.GetID ()));

                // Send the User object back to the client
                Client.sendEvent (SocketIOEvents.LOGIN.GetEventName (), Target);
            }
        });

        Server.addEventListener (SocketIOEvents.REGISTER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                LoginRegisterData Data = (LoginRegisterData) CreateClass (LoginRegisterData.class, Args);

                User RegisterUser = new User (Helper.GenerateToken (28), UUID.randomUUID (), Data.GetUsername (), Data.GetPassword (), new ArrayList<UUID> ());
                Object Result = DBHelper.AddUser (RegisterUser);

                // TODO: Fix `Invalid UUID string` error
                if (Result instanceof User)
                {
                    User Target = (User) Result;
                    Tracker.AddSession (new Session (Target.GetToken (), Client.getSessionId (), Target.GetID ()));

                    // Send the User object back to the client
                    Client.sendEvent (SocketIOEvents.REGISTER.GetEventName (), Target.ToMap ());
                }

                else if (Result instanceof Error)
                {
                    Error Target = (Error) Result;

                    // Send the User object back to the client
                    Client.sendEvent (SocketIOEvents.REGISTER.GetEventName (), Target.ToMap ());
                }
            }
        });

        Server.addEventListener(SocketIOEvents.GET_PASSWORD_SALT.GetEventName (), String.class, new DataListener<String> () {
            @Override
            public void onData (SocketIOClient Client, String Data, AckRequest Request) throws Exception {
                String Password = DBHelper.GetPassword (Data);

                String Salt = Helper.GetSalt (Password);

                Client.sendEvent (SocketIOEvents.GET_PASSWORD_SALT.GetEventName (), Salt);
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

        Server.addEventListener(SocketIOEvents.GET_ROOM.GetEventName (), String.class, new DataListener<String> () {
            @Override
            public void onData (SocketIOClient Client, String Data, AckRequest Request) throws Exception
            {
                if (DBHelper.RoomExists (UUID.fromString (Data)))
                {
                    Client.sendEvent (SocketIOEvents.GET_ROOM.GetEventName (), DBHelper.GetRoom (UUID.fromString (Data)).ToMap ());
                }
                else
                {
                    Client.sendEvent (SocketIOEvents.GET_ROOM_NAME.GetEventName (), new Error (ErrorType.RoomNotFound, "Room with the ID was not found: " + Data));
                }
            }
        });

        Server.addEventListener(SocketIOEvents.GET_ROOM_NAME.GetEventName (), String.class, new DataListener<String> () {
            @Override
            public void onData (SocketIOClient Client, String Data, AckRequest Request) throws Exception
            {
                if (DBHelper.RoomExists (UUID.fromString (Data)))
                {
                    Client.sendEvent (SocketIOEvents.GET_ROOM_NAME.GetEventName (), DBHelper.GetRoom (UUID.fromString (Data)).GetDisplay ());
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.GET_ROOM_NAME.GetEventName (), new Error (ErrorType.RoomNotFound, "Room with the ID was not found: " + Data));
                }
            }
        });

        Server.addEventListener(SocketIOEvents.GET_USER.GetEventName (), String.class, new DataListener<String> () {
            @Override
            public void onData (SocketIOClient Client, String Data, AckRequest Request) throws Exception
            {
                if (DBHelper.UserExists (UUID.fromString (Data)))
                {
                    Client.sendEvent (SocketIOEvents.GET_USER.GetEventName (), DBHelper.GetUser (UUID.fromString (Data)).ToMap ());
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.GET_USER.GetEventName (), new Error (ErrorType.UserNotFound, "User with the ID was not found: " + Data));
                }
            }
        });

        Server.addEventListener(SocketIOEvents.GET_USER_NAME.GetEventName (), String.class, new DataListener<String> () {
            @Override
            public void onData (SocketIOClient Client, String Data, AckRequest Request) throws Exception
            {
                if (DBHelper.UserExists (UUID.fromString (Data)))
                {
                    Client.sendEvent (SocketIOEvents.GET_USER_NAME.GetEventName (), DBHelper.GetUser (UUID.fromString (Data)).GetUsername ());
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.GET_USER_NAME.GetEventName (), new Error (ErrorType.UserNotFound, "User with the ID was not found: " + Data));
                }
            }
        });
    }

}
