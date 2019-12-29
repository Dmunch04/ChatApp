package org.DevNex.ChatApp.Server;

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
    private String SocketPort;

    public Server (String Host, String Port, String SocketPort)
    {
        this.Host = Host;
        this.Port = Port;
        this.SocketPort = SocketPort;

        Configuration Config = new Configuration ();
        Config.setHostname (Host);
        Config.setPort (Integer.parseInt (SocketPort));
        SocketConfig SocketConfig = Config.getSocketConfig ();
        SocketConfig.setReuseAddress (true);
        Config.setSocketConfig (SocketConfig);

        this.App = new Express ();
        this.SocketIO = new SocketIOServer (Config);

        try { App.use (Middleware.statics (this.getClass ().getResource ("/Static/dist/").getPath ())); }
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
