var Socket = io ();

Socket.on ('load-room', (Room) => {
  console.log (Room.ID);
  console.log (Room.Clients);
});

var Room = {
  ID: 0,
  Creator: '',
  Clients: {},
  Messages: {},
}

document.getElementById ('BTN_CREATE').addEventListener ('click', () => {
  var User = document.getElementById ('USER').value;
  if (User.trim === '') UsernameError (User);

  var ID = [];
  var Index;
  for (Index = 0; Index < 6; Index++)
  {
    ID[Index] = Math.floor (Math.random () * 10);
  }

  Room.ID = parseInt (ID.join (''));
  Room.Creator = User;

  Socket.emit ('create-room', Room);
});

document.getElementById ('BTN_JOIN').addEventListener ('click', () => {
  var User = document.getElementById ('USER').value;
  if (User.trim === '') UsernameError (User);

  var Code = document.getElementById ('JOIN_CODE').value;
  if (Code.trim === '') CodeError (Code);

  var Data = {
    Code: parseInt (Code),
    Display: User,
  }

  Socket.emit ('join-room', Data);
});

function UsernameError (Username)
{
  // handle
}

function CodeError (Code)
{
  // handle
}
