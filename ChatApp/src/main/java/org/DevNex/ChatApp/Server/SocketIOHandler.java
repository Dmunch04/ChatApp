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

        Server.addEventListener (SocketIOEvents.LOGIN.GetEventName (), LoginRegisterData.class, new DataListener<LoginRegisterData> () {
            @Override
            public void onData (SocketIOClient Client, LoginRegisterData Data, AckRequest Request)
            {
                // TODO: Login

                // TODO: Replace the `""` with the users ID, obtained by getting from DB using the Token
                //Tracker.AddSession (new Session (Data.GetToken (), Client.getSessionId (), ""));

                // TODO: Fix that every variable is always null in the data
                System.out.println ("Token: " + Data.GetToken ());
            }
        });

        Server.addEventListener (SocketIOEvents.REGISTER.GetEventName (), LoginRegisterData.class, new DataListener<LoginRegisterData> () {
            @Override
            public void onData (SocketIOClient Client, LoginRegisterData Data, AckRequest Request)
            {
                // TODO: Register

                // TODO: Replace the `""` with the users ID, obtained by getting from DB using the Token
                Tracker.AddSession (new Session (Data.GetToken (), Client.getSessionId (), ""));
            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_USER.GetEventName (), RemoveUserData.class, new DataListener<RemoveUserData> () {
            @Override
            public void onData (SocketIOClient Client, RemoveUserData Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.CREATE_ROOM.GetEventName (), CreateRoomData.class, new DataListener<CreateRoomData> () {
            @Override
            public void onData (SocketIOClient Client, CreateRoomData Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.JOIN_ROOM.GetEventName (), JoinRemoveRoomData.class, new DataListener<JoinRemoveRoomData> () {
            @Override
            public void onData (SocketIOClient Client, JoinRemoveRoomData Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_ROOM.GetEventName (), JoinRemoveRoomData.class, new DataListener<JoinRemoveRoomData> () {
            @Override
            public void onData (SocketIOClient Client, JoinRemoveRoomData Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.SEND_MESSAGE.GetEventName (), SendMessageData.class, new DataListener<SendMessageData> () {
            @Override
            public void onData (SocketIOClient Client, SendMessageData Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_MESSAGE.GetEventName (), RemoveMessageData.class, new DataListener<RemoveMessageData> () {
            @Override
            public void onData (SocketIOClient Client, RemoveMessageData Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.KICK_USER.GetEventName (), KickUserData.class, new DataListener<KickUserData> () {
            @Override
            public void onData (SocketIOClient Client, KickUserData Data, AckRequest Request)
            {

            }
        });
    }

}
