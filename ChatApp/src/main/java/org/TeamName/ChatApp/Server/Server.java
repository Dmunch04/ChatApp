package org.TeamName.ChatApp.Server;

import express.Express;
import express.middleware.Middleware;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import me.Munchii.SocketioServer.SocketIoNamespace;
import me.Munchii.SocketioServer.SocketIoServer;
import me.Munchii.SocketioServer.SocketIoSocket;

import java.io.IOException;
import java.net.URISyntaxException;

public class Server
{

    /*
        We're working on the socket.io part (server) and ain't done yet. It will be added in next commit. This commit is purely for testing purpose!
    */

    private Express App;
    private Socket SocketIO;

    private String Host;
    private String Port;

    public Server (String Host, String Port)
    {
        this.Host = Host;
        this.Port = Port;

        this.App = new Express ();
        try { this.SocketIO = IO.socket (Host); }
        catch (URISyntaxException Error) { Error.printStackTrace (); }

        try { App.use (Middleware.statics (this.getClass ().getResource ("/Static").getPath ())); }
        catch (IOException Error) { Error.printStackTrace (); }

        /*
        SocketIO.on (Socket.EVENT_CONNECT, new Emitter.Listener () {
            @Override
            public void call (Object... Objects)
            {
                System.out.println ("dtsad");
                SocketIO.emit ("test", "yeet");
            }
        }).on("hello", new Emitter.Listener () {
            @Override
            public void call (Object... Objects)
            {
                System.out.println (Objects);
            }
        });
        */

        /*
        SocketIoServer Server = new SocketIoServer (new EngineIoServer ());
        SocketIoNamespace Namespace = Server.namespace ("/");
        Namespace.on("connection", new Emitter.Listener () {
            @Override
            public void call(Object... Args)
            {
                SocketIoSocket Socket = (SocketIoSocket) Args[0];
                System.out.println (Socket.getId ());
            }
        });
        */

        App.bind (new ServerBindings());
        App.listen (Integer.parseInt (Port));

        System.out.println ("Server running on: " + Host + ":" + Port + "!");
    }

}
