var Sock = io ('http://localhost:7089');

//Sock.emit ('get-salt', 'Munchii');
//Sock.on ('get-salt', (Salt) => { console.log (Salt); });

Sock.emit ('register', { Username: 'Munchii', Password: '$2y$12$9UGnuDVlOovPhuKzo5xfN.jcinpaGu19l4dZsTAxFOTE6a.c.HxSO' });
Sock.on ('register', (User) => {
    console.log (User);
});