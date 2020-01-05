let sock = io("http://localhost:7089");

//Sock.emit ('get-salt', 'Munchii');
//Sock.on ('get-salt', (Salt) => { console.log (Salt); });

Sock.emit ('register', { Username: 'hai', Password: '$2y$12$r0tFW773PKrNbkmOfZuSeeu2/v3YeaNo3SRaQBfRFIMjWhSrHTMIC' });
Sock.on ('register', (User) => {
    console.log (User);
});