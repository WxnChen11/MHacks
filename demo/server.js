// Generated by CoffeeScript 1.10.0
(function() {
  var Paparazzo, http, paparazzo, updatedImage, url;

  Paparazzo = require('../src/paparazzo');

  http = require('http');

  url = require('url');

  paparazzo = new Paparazzo({
    host: '35.2.58.188',
    port: 8081,
    path: '/'
  });

  updatedImage = '';

  paparazzo.on("update", (function(_this) {
    return function(image) {
      updatedImage = image;
      return console.log("Downloaded " + image.length + " bytes");
    };
  })(this));

  paparazzo.on('error', (function(_this) {
    return function(error) {
      return console.log("Error: " + error.message);
    };
  })(this));

  paparazzo.start();

  http.createServer(function(req, res) {
    var data, path;
    data = '';
    path = url.parse(req.url).pathname;
    if (path === '/camera' && (updatedImage != null)) {
      data = updatedImage;
      console.log("Will serve image of " + data.length + " bytes");
    }
    res.writeHead(200, {
      'Content-Type': 'image/jpeg',
      'Content-Length': data.length
    });
    res.write(data, 'binary');
    return res.end();
  }).listen(3000);

}).call(this);
