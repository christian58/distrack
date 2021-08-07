'use strict';

var mysql2 = require('mysql2');

var connection = mysql2.createConnection({
  	host: '190.12.73.84',
  	user: 'amoviles',
  	password: 'am12345',
  	port: 21580//,
  	//database: 'appdistrack'
});

module.exports.appdistrack_mysql2 = connection;