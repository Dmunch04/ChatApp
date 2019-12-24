// TODO: Handle disconnection
// TODO: Switch HTML page when joining/creating room
// TODO: Make client function for sending and recieving (+ showing) messages
// TODO: Optimize code
// TODO: Make UI prettier



const PORT = 6969;

var Express = require ('express');
var HTTP = require ('http');
var Path = require ('path');
var SocketIO = require ('socket.io');

var App = Express ();
var Server = HTTP.Server (App);
var IO = SocketIO (Server);

App.set ('port', PORT);
App.use ('/Static', Express.static (__dirname + '/Static'));
App.get ('/', (Request, Response) => {
  Response.sendFile (Path.join (__dirname, 'Static/index.html'));
});

Server.listen (PORT, () => {
  console.log ('Starting server on port', PORT);
});

var Rooms = {};
IO.on ('connection', (Client) => {
  Client.on ('create-room', (Room) => {
    Room.Clients[Client.id] = {
      ID: Client.id,
      Display: Room.Creator,
    };
    Rooms[Room.ID] = Room;

    Client.emit ('load-room', Room);
  });

  Client.on ('join-room', (Data) => {
    if (Data.Code in Rooms)
    {
      var Room = Rooms[Data.Code];
      Room.Clients[Client.id] = {
        ID: Client.id,
        Display: Data.Display,
      }
      Rooms[Data.Code] = Room;

      for (User in Room.Clients)
      {
        IO.sockets.sockets[User].emit ('load-room', Room);
      }
    }
  });

  Client.on ('message-send', (Data) => {
    var Room = Rooms[Data.RoomID];

    var ID = [];
    var Index;
    for (Index = 0; Index < 28; Index++)
    {
      ID[Index] = Math.floor (Math.random () * 10);
    }
    ID = parseInt (ID.join (''));

    Room.Messages[ID] = {
      ID: ID,
      Sender: Data.Sender,
      Message: Data.Message,
      Timestamp: Date.now (),
    }

    Rooms[Data.RoomID] = Room;

    for (User in Room.Clients)
    {
      IO.sockets.sockets[User].emit ('load-room', Room);
    }
  });
});
