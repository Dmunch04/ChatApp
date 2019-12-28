package org.TeamName.ChatApp.Server;

import com.corundumstudio.socketio.*;
import express.Express;
import express.middleware.Middleware;

import java.io.IOException;

public class Server
{

    private Express App;
    private SocketIOServer SocketIO;

    private String Host;
    private String Port;

    public Server (String Host, String Port)
    {
        this.Host = Host;
        this.Port = Port;

        Configuration Config = new Configuration ();
        Config.setHostname (Host);
        Config.setPort (Integer.parseInt (Port));
        // TODO: Fix this. It still won't work properly. I got it to work before moving into Server.java. Source: https://github.com/mrniko/netty-socketio/issues/490
        SocketConfig SocketConfig = Config.getSocketConfig ();
        SocketConfig.setReuseAddress (true);
        Config.setSocketConfig (SocketConfig);

        this.App = new Express ();
        this.SocketIO = new SocketIOServer (Config);

        try { App.use (Middleware.statics (this.getClass ().getResource ("/Static").getPath ())); }
        catch (IOException Error) { Error.printStackTrace (); }

        App.bind (new ServerBindings());
        App.listen (Integer.parseInt (Port));

        new SocketIOHandler (SocketIO);

        System.out.println ("Server running on: " + Host + ":" + Port + "!");

        SocketIO.start ();
        try { Thread.sleep (Integer.MAX_VALUE); }
        catch (InterruptedException Error) { Error.printStackTrace (); }
        SocketIO.stop ();
    }

}
