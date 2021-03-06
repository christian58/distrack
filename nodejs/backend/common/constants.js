var ip = require('ip');

var config = {
    ipserver: ip.address(), // 0.0.0.0
    port: 3010,
    ENV_DEV: true
}

/* var mongodb = {
    username: 'undefined',
    password: 'undefined',
    database: 'distrackDB',
    connectionstring: 'mongodb://localhost/websocketDB'
} */

var configlog = {
    so: '',
    pathapp: '/logs/app.log',
    patherror: '/logs/error.log',
    windowspathapp: 'D:/logs/distrack-services/app.log',
    windowspatherr: 'D:/logs/distrack-services/error.log',
    linuxpathapp: '/var/log/distrack-services/app.log',
    linuxpatherr: '/var/log/distrack-services/error.log',
}

var constantsvalues = {
    LINUX: 'linux',
    WINDOWS: 'windows'
}

var tables = {
    PEDIDOS: 1
}



module.exports.config = config;
//module.exports.mongodb = mongodb;
module.exports.configlog = configlog;
module.exports.constantsvalues = constantsvalues;
module.exports.tables = tables;