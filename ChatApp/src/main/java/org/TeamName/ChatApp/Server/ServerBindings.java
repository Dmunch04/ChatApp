package org.TeamName.ChatApp.Server;

import express.DynExpress;
import express.http.request.Request;
import express.http.response.Response;

import java.nio.file.Paths;

public class ServerBindings
{

    @DynExpress (context = "/")
    public void GetIndex (Request Request, Response Response)
    {
        Response.send (Paths.get (this.getClass ().getResource ("/Static/index.html").getPath ()));
    }

}
