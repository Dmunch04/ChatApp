var Sock = io ('http://localhost:7089');

Sock.emit ('get-salt', 'Munchii');
Sock.on ('get-salt', (Salt) => { console.log (Salt); });