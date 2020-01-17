package org.DevNex.ChatApp.Server;

import express.DynExpress;
import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;

import java.nio.file.Paths;
import java.util.UUID;

public class ServerBindings
{

    @DynExpress (context = "*")
    public void GetIndex (Request Request, Response Response)
    {
        Response.send (Paths.get (this.getClass ().getResource ("/Static/dist/index.html").getPath ()));
        //Response.send (Paths.get (this.getClass ().getResource ("/TestClient/index.html").getPath ()));
    }


    /* API Handlers
     * These will handle API requests
     */

    @DynExpress (context = "/api/rooms", method = RequestMethod.POST)
    public void CreateRoom (Request Request, Response Response)
    {
        String Token = Request.getHeader ("Token").get (0); // Might not work
        // TODO: Validate token, and do it in the other functions as well
        // TODO: Find a good way to make helper functions that do these things (create room, send message, get user, etc.) and also use them in socket events too
    }

    @DynExpress (context = "/api/rooms/:id", method = RequestMethod.GET)
    public void GetRoom (Request Request, Response Response)
    {
        UUID RoomID = UUID.fromString (Request.getParam ("id"));
    }

    @DynExpress (context = "/api/rooms/:id/messages", method = RequestMethod.POST)
    public void PostMessage (Request Request, Response Response)
    {
        UUID RoomID = UUID.fromString (Request.getParam ("id"));
    }

    @DynExpress (context = "/api/rooms/:id/messages/:messageid", method = RequestMethod.GET)
    public void GetMessage (Request Request, Response Response)
    {
        UUID RoomID = UUID.fromString (Request.getParam ("id"));
        UUID MessageID = UUID.fromString (Request.getParam ("messageid"));
    }

    @DynExpress (context = "/api/rooms/:id/users/:userid", method = RequestMethod.GET)
    public void GetUser (Request Request, Response Response)
    {
        UUID RoomID = UUID.fromString (Request.getParam ("id"));
        UUID UserID = UUID.fromString (Request.getParam ("userid"));
    }

}
