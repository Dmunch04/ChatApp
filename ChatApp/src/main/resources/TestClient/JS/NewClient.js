var Sock = io ("http://localhost:7089");
var iUser = null;

function login () {
    var username = document.getElementById ('username').value;
    var password = document.getElementById ('password').value;

    Sock.emit ('login', { Username: username, Password: password });
    Sock.on ('login', (User) => {
        console.log (User);
        iUser = User;

/*
        var Messages = document.getElementById ('messageslist');
        var MessageItem = document.createElement ('li');
        MessageItem.style.cssText = 'font-size: 14px';
        MessageItem.appendChild (document.createTextNode (User.Username + ' connected!'));
        Messages.appendChild (MessageItem);
*/
    });
}

function sendmsg () {
    var msg = document.getElementById ('msg').value;

    Sock.emit ('send-message', { Token: iUser.Token, UserID: iUser.ID, RoomID: 'df298a5b-6e2d-4838-bda5-fb564ce0cdb8', Message: msg });
}

Sock.on ('send-message', (Message) => {
    console.log (Message);
    Sock.emit ('get-user-name', Message.SenderID);
    Sock.on ('get-user-name', (Username) => {
        console.log (Username + ": " + JSON.stringify (Message.Content));
        document.getElementById ('messages').value = '';
        var Messages = document.getElementById ('messageslist');
        var MessageItem = document.createElement ('li');
        MessageItem.appendChild (document.createTextNode (Username + ": " + Message.Content));
        Messages.appendChild (MessageItem);
        console.log ('end of this message received, expect a new soon!')
    });
});