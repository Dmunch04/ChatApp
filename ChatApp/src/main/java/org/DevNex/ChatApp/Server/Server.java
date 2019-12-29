package org.DevNex.ChatApp.Server;

import com.corundumstudio.socketio.*;
import express.Express;
import express.middleware.Middleware;
import org.DevNex.ChatApp.Sessions.SessionTracker;

import java.io.IOException;
import java.io.InputStream;

public class Server
{

    private Express App;
    private SocketIOServer SocketIO;

    private SessionTracker Tracker;

    private String Host;
    private String Port;
    private String SocketPort;

    public Server (String Host, String Port, String SocketPort)
    {
        this.Host = Host;
        this.Port = Port;
        this.SocketPort = SocketPort;

        this.Tracker = new SessionTracker ();

        // Server Config
        Configuration Config = new Configuration ();
        Config.setHostname (Host);
        Config.setPort (Integer.parseInt (SocketPort));

        // Server Socket Config
        SocketConfig SocketConfig = Config.getSocketConfig ();
        SocketConfig.setReuseAddress (true);
        Config.setSocketConfig (SocketConfig);

        // Server SSL Config
        // TODO: Fix error when using SSL
        //Config.setKeyStorePassword ("test1234");
        //Config.setKeyStore (this.getClass ().getResourceAsStream ("/Keystore.jks"));

        this.App = new Express ();
        this.SocketIO = new SocketIOServer (Config);

        try { App.use (Middleware.statics (this.getClass ().getResource ("/Static").getPath ())); }
        catch (IOException Error) { Error.printStackTrace (); }

        App.bind (new ServerBindings());
        App.listen (Integer.parseInt (Port));

        new SocketIOHandler (this, SocketIO);

        SocketIO.start ();

        System.out.println ("Server running on: " + Host + ":" + Port + "!");

        try { Thread.sleep (Integer.MAX_VALUE); }
        catch (InterruptedException Error) { Error.printStackTrace (); }

        SocketIO.stop ();
    }

    public SessionTracker GetSessionTracker ()
    {
        return Tracker;
    }

}
