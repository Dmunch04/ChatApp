var Sock = io ("http://localhost:7089");

//Sock.emit ('get-salt', 'Munchii');
//Sock.on ('get-salt', (Salt) => { console.log (Salt); });

//Sock.emit ('register', { Username: 'hai', Password: '$2y$12$r0tFW773PKrNbkmOfZuSeeu2/v3YeaNo3SRaQBfRFIMjWhSrHTMIC' });
//Sock.on ('register', (User) => {
    //console.log (User);
//});

//Sock.emit ('get-salt', 'Munchii');
//Sock.on ('get-salt', (Result) => {
    //console.log (Result);
//});

//Sock.emit ('login', { Username: 'Munchii', Password: '$2y$12$9UGnuDVlOovPhuKzo5xfN.jcinpaGu19l4dZsTAxFOTE6a.c.HxSO' });
//Sock.on ('login', (Result) => {
    //console.log (Result);
//});

Sock.emit ('login', { Username: 'Munchii', Password: '$2y$12$9UGnuDVlOovPhuKzo5xfN.jcinpaGu19l4dZsTAxFOTE6a.c.HxSO' });
Sock.on ('login', (Result) => {
    console.log (Result);
    var User = Result;
    Sock.emit ('create-room', { Token: Result.Token, Display: 'TestServer', UserID: Result.ID })
    Sock.on ('create-room', (Result) => {
        console.log (Result);
        Sock.emit ('send-message', { Token: User.Token, UserID: User.ID, RoomID: Result.ID, Message: 'Test!' });
        Sock.on ('send-message', (Result) => {
            console.log (Result);
        });
    });
});

/*
{Content: "Test!", ID: "1942623e-2b4b-4758-afc9-6cf56d68ec3d",
RoomID: "10043974-be72-4db6-a255-06076ed49ad0",
SenderID: "def788ff-8f3e-46cd-aa15-ac7eed0386e5"}
 */
