package org.DevNex.ChatApp.Server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import org.DevNex.ChatApp.App;
import org.DevNex.ChatApp.Database.DatabaseHelper;
import org.DevNex.ChatApp.ErrorSystem.Error;
import org.DevNex.ChatApp.ErrorSystem.ErrorType;
import org.DevNex.ChatApp.Objects.Data.*;
import org.DevNex.ChatApp.Objects.Message;
import org.DevNex.ChatApp.Objects.Room;
import org.DevNex.ChatApp.Objects.User;
import org.DevNex.ChatApp.Objects.UserStatus;
import org.DevNex.ChatApp.Sessions.ActionType;
import org.DevNex.ChatApp.Sessions.Session;
import org.DevNex.ChatApp.Sessions.SessionTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.DevNex.ChatApp.StorageServer.SpaceHelper;
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
    private static DatabaseHelper DBHelperClone;
    private SpaceHelper StorageHelper;

    public SocketIOHandler (Server Instance, SocketIOServer Server)
    {
        this.Instance = Instance;
        this.Server = Server;
        this.Tracker = Instance.GetSessionTracker ();
        this.DBHelper = Instance.GetDatabaseHelper ();
        this.DBHelperClone = DBHelper;
        this.StorageHelper = App.GetSpaceHelper ();

        Listen ();
    }

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

                Object Result = DBHelper.GetUser (Data.GetUsername ());

                if (Result instanceof User)
                {
                    User Target = (User) Result;

                    if (Target.GetPassword ().equals (Data.GetPassword ()))
                    {
                        Tracker.AddSession (new Session (Target.GetToken (), Client.getSessionId (), Target.GetID ()));

                        // Send the User object back to the client
                        Client.sendEvent (SocketIOEvents.LOGIN.GetEventName (), Target.ToMap ());
                    }

                    else
                    {
                        // Send the User object back to the client
                        Client.sendEvent (SocketIOEvents.LOGIN.GetEventName (), new Error (ErrorType.LoginFail, "Wrong password entered!").ToMap ());
                    }
                }

                else if (Result instanceof Error)
                {
                    Error Target = (Error) Result;

                    // Send the Error object back to the client
                    Client.sendEvent (SocketIOEvents.LOGIN.GetEventName (), Target.ToMap ());
                }
            }
        });

        Server.addEventListener (SocketIOEvents.REGISTER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                LoginRegisterData Data = (LoginRegisterData) CreateClass (LoginRegisterData.class, Args);

                User RegisterUser = new User (Helper.GenerateToken (28), UUID.randomUUID (), Data.GetUsername (), Data.GetPassword (), Helper.DefaultIconID, new ArrayList<UUID> (), UserStatus.ONLINE);
                Object Result = DBHelper.AddUser (RegisterUser);

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
                if (DBHelper.UserExists (Data))
                {
                    String Password = DBHelper.GetPassword (Data);

                    String Salt = Helper.GetSalt (Password);

                    Client.sendEvent (SocketIOEvents.GET_PASSWORD_SALT.GetEventName (), Salt);
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.GET_PASSWORD_SALT.GetEventName (), new Error (ErrorType.UserNotFound, "User with username not found: " + Data));
                }
            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_USER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                RemoveUserData Data = (RemoveUserData) CreateClass (RemoveUserData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    Room TargetRoom = DBHelper.GetRoom (Data.GetRoomID ());
                    if (TargetRoom.GetClients ().contains (Data.GetUserID ()))
                    {
                        TargetRoom.RemoveClient (Data.GetUserID ());
                        DBHelper.UpdateRoom (TargetRoom);

                        Client.sendEvent(SocketIOEvents.REMOVE_USER.GetEventName (), TargetRoom.ToMap ());
                    }

                    else
                    {
                        Client.sendEvent (SocketIOEvents.REMOVE_USER.GetEventName (), new Error (ErrorType.UserNotFound, "User not in room!").ToMap ());
                    }
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.REMOVE_USER.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
                }
            }
        });

        Server.addEventListener (SocketIOEvents.CREATE_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                CreateRoomData Data = (CreateRoomData) CreateClass (CreateRoomData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    Room TargetRoom = new Room (UUID.randomUUID (), Data.GetDisplay (), Data.GetUserID (), Helper.DefaultIconID, new ArrayList<> (), new HashMap<> ());
                    TargetRoom.AddClient (Data.GetUserID ());
                    DBHelper.AddRoom (TargetRoom);

                    User TargetUser = DBHelper.GetUser (Data.GetUserID ());
                    TargetUser.AddRoom (TargetRoom.GetID ());
                    DBHelper.UpdateUser (TargetUser);

                    Client.sendEvent (SocketIOEvents.CREATE_ROOM.GetEventName (), TargetRoom.ToMap ());
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.CREATE_ROOM.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
                }
            }
        });

        Server.addEventListener (SocketIOEvents.JOIN_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                JoinRemoveRoomData Data = (JoinRemoveRoomData) CreateClass (JoinRemoveRoomData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    Room TargetRoom = DBHelper.GetRoom (Data.GetRoomID ());
                    TargetRoom.AddClient (Data.GetUserID ()); // TODO: Maybe add a check to see if the user is already in the room and send an error if the user is, but Idk
                    DBHelper.UpdateRoom (TargetRoom);

                    User TargetUser = DBHelper.GetUser (Data.GetUserID ());
                    TargetUser.AddRoom (Data.GetRoomID ()); // Same thing for here I guess
                    DBHelper.UpdateUser (TargetUser);

                    Client.sendEvent (SocketIOEvents.JOIN_ROOM.GetEventName (), TargetUser.ToMap ());
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.JOIN_ROOM.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
                }
            }
        });

        Server.addEventListener (SocketIOEvents.LEAVE_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                JoinRemoveRoomData Data = (JoinRemoveRoomData) CreateClass (JoinRemoveRoomData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    if (DBHelper.RoomExists (Data.GetRoomID ()))
                    {
                        Room TargetRoom = DBHelper.GetRoom (Data.GetRoomID ());

                        if (TargetRoom.HasClient (Data.GetUserID ()))
                        {
                            TargetRoom.RemoveClient (Data.GetUserID ());
                            DBHelper.UpdateRoom (TargetRoom);

                            User TargetUser = DBHelper.GetUser (Data.GetUserID ());
                            TargetUser.RemoveRoom (Data.GetRoomID ());
                            DBHelper.UpdateUser (TargetUser);

                            Client.sendEvent (SocketIOEvents.LEAVE_ROOM.GetEventName (), TargetUser.ToMap ());
                        }

                        else
                        {
                            Client.sendEvent (SocketIOEvents.LEAVE_ROOM.GetEventName (), new Error (ErrorType.UserNotFound, "User with ID was not found in server: " + Data.GetUserID ().toString ()).ToMap ());
                        }
                    }

                    else
                    {
                        Client.sendEvent (SocketIOEvents.LEAVE_ROOM.GetEventName (), new Error (ErrorType.RoomNotFound, "Room with ID was not found: " + Data.GetRoomID ().toString ()).ToMap ());
                    }
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.LEAVE_ROOM.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
                }
            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                JoinRemoveRoomData Data = (JoinRemoveRoomData) CreateClass (JoinRemoveRoomData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    if (DBHelper.RoomExists (Data.GetRoomID ()))
                    {
                        Room TargetRoom = DBHelper.GetRoom (Data.GetRoomID ());

                        if (TargetRoom.GetCreator ().equals (Data.GetUserID ()))
                        {
                            for (UUID UserID : TargetRoom.GetClients ())
                            {
                                User TargetUser = DBHelper.GetUser (UserID);
                                TargetUser.RemoveRoom (Data.GetRoomID ());
                                DBHelper.UpdateUser (TargetUser);

                                Server.getClient (Tracker.GetSessionByToken (TargetUser.GetToken ()).GetSessionID ()).sendEvent (SocketIOEvents.REMOVE_ROOM.GetEventName (), TargetUser.ToMap ());
                            }

                            DBHelper.RemoveRoom (Data.GetRoomID ());
                        }

                        else
                        {
                            Client.sendEvent (SocketIOEvents.REMOVE_ROOM.GetEventName (), new Error (ErrorType.NoUserPermission, "User does not have the required permissions!").ToMap ());
                        }
                    }

                    else
                    {
                        Client.sendEvent (SocketIOEvents.REMOVE_ROOM.GetEventName (), new Error (ErrorType.RoomNotFound, "Room with ID was not found: " + Data.GetRoomID ().toString ()).ToMap ());
                    }
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.REMOVE_ROOM.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
                }
            }
        });

        Server.addEventListener (SocketIOEvents.SEND_MESSAGE.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request)
            {
                SendMessageData Data = (SendMessageData) CreateClass (SendMessageData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    if (DBHelper.RoomExists (Data.GetRoomID ()))
                    {
                        Message TargetMessage = new Message (UUID.randomUUID (), Data.GetRoomID (), Data.GetUserID (), Data.GetMessage ());
                        DBHelper.AddMessage (TargetMessage);

                        Room TargetRoom = DBHelper.GetRoom (Data.GetRoomID ());
                        TargetRoom.AddMessage (TargetMessage.GetID (), TargetMessage);
                        DBHelper.UpdateRoom (TargetRoom);

                        for (UUID UserID : TargetRoom.GetClients ())
                        {
                            if (Tracker.HasSession (UserID))
                            {
                                //Server.getClient (Tracker.GetSessionByToken (Data.GetToken ()).GetSessionID ()).sendEvent (SocketIOEvents.SEND_MESSAGE.GetEventName (), TargetMessage.ToMap ());
                                System.out.println (TargetMessage.ToMap ());
                                Server.getClient (Tracker.GetSessionByID (UserID).GetSessionID ()).sendEvent (SocketIOEvents.SEND_MESSAGE.GetEventName (), TargetMessage.ToMap ());
                            }
                        }
                    }

                    else
                    {
                        Client.sendEvent (SocketIOEvents.SEND_MESSAGE.GetEventName (), new Error (ErrorType.RoomNotFound, "Room with ID not found: " + Data.GetRoomID ().toString ()).ToMap ());
                    }
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.SEND_MESSAGE.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
                }
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

        Server.addEventListener(SocketIOEvents.GET_ROOM.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request) throws Exception
            {
                StringData Data = (StringData) CreateClass (StringData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    if (DBHelper.RoomExists (UUID.fromString (Data.GetData ())))
                    {
                        Client.sendEvent (SocketIOEvents.GET_ROOM.GetEventName (), DBHelper.GetRoom (UUID.fromString (Data.GetData ())).ToMap ());
                    }
                    else
                    {
                        Client.sendEvent (SocketIOEvents.GET_ROOM.GetEventName (), new Error (ErrorType.RoomNotFound, "Room with the ID was not found: " + Data));
                    }
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.GET_ROOM.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
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

        Server.addEventListener(SocketIOEvents.GET_USER.GetEventName (), Map.class, new DataListener<Map> () {
            @Override
            public void onData (SocketIOClient Client, Map Args, AckRequest Request) throws Exception
            {
                StringData Data = (StringData) CreateClass (StringData.class, Args);

                if (SessionExists (Data.GetToken (), Data.GetUserID (), Client.getSessionId ()))
                {
                    if (DBHelper.UserExists (UUID.fromString (Data.GetData ())))
                    {
                        User Target = DBHelper.GetUser (UUID.fromString (Data.GetData ()));
                        if (Tracker.GetSessionByID (Target.GetID ()) == null) Target.SetStatus (UserStatus.OFFLINE);
                        Client.sendEvent (SocketIOEvents.GET_USER.GetEventName (), Target.ToMap ());
                    }

                    else
                    {
                        Client.sendEvent (SocketIOEvents.GET_USER.GetEventName (), new Error (ErrorType.UserNotFound, "User with the ID was not found: " + Data));
                    }
                }

                else
                {
                    Client.sendEvent (SocketIOEvents.GET_USER.GetEventName (), new Error (ErrorType.InvalidSession, "Current session is not valid!").ToMap ());
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

    private boolean SessionExists (String Token, UUID UserID, UUID ClientID)
    {
        boolean TokenMatch = Tracker.GetSessionByToken (Token) != null;
        boolean IDMatch = Tracker.GetSessionByID (UserID) != null;
        boolean ClientIDMatch = Tracker.GetSession (ClientID) != null;
        boolean UserTokenMatch = DBHelper.GetUser (UserID).GetToken ().equals (Token);
        boolean UserIDMatch = DBHelper.GetUser (UserID).GetID ().equals (UserID);

        return TokenMatch && IDMatch && ClientIDMatch && UserTokenMatch && UserIDMatch;
    }

    public static DatabaseHelper GetDBHelper ()
    {
        return DBHelperClone;
    }

}
