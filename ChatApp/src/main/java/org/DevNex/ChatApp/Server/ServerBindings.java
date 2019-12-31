package org.DevNex.ChatApp.Server;

import express.DynExpress;
import express.http.request.Request;
import express.http.response.Response;

import java.nio.file.Paths;

public class ServerBindings
{

    @DynExpress (context = "*")  // catch all rule
    public void GetIndex (Request Request, Response Response)
    {
        Response.send (Paths.get (this.getClass ().getResource ("/Static/dist/index.html").getPath ()));
        //Response.send (Paths.get (this.getClass ().getResource ("/TestClient/index.html").getPath ()));
    }

}
