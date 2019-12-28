package org.TeamName.ChatApp.Server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

import org.TeamName.ChatApp.Objects.Data;
import org.TeamName.ChatApp.Objects.Image;
import org.TeamName.ChatApp.Objects.Data;
import org.TeamName.ChatApp.Objects.Room;
import org.TeamName.ChatApp.Objects.User;

public class SocketIOHandler
{

    private SocketIOServer Server;

    public SocketIOHandler (SocketIOServer Server)
    {
        this.Server = Server;

        Listen ();
    }

    private void Listen ()
    {
        Server.addConnectListener(new ConnectListener () {
            @Override
            public void onConnect (SocketIOClient Client)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.LOGIN.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.REGISTER.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_USER.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.CREATE_ROOM.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.JOIN_ROOM.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_ROOM.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.SEND_MESSAGE.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.REMOVE_MESSAGE.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });

        Server.addEventListener (SocketIOEvents.KICK_USER.GetEventName (), Data.class, new DataListener<Data> () {
            @Override
            public void onData (SocketIOClient Client, Data Data, AckRequest Request)
            {

            }
        });
    }

}
