'use strict';

var mongoose = require('mongoose');
const util = require('util');
var logger = require('log4js').getLogger('conexionMongo');

// 'mongodb://localhost/database'

// ============================================================
// configuracion conexion -> appdistrack
// ============================================================

var appdistrack = {
    config: {
        host: '127.0.0.1',
        port: 27017,
        database: 'appdistrack',
        user: undefined,
        password: undefined
    },
    createConnection: function() {
        var db = mongoose.createConnection(util.format('mongodb://%s:%s/%s', this.config.host, this.config.port, this.config.database));

        db.on('error', function() {
            logger.error(util.format('Error! Database %s connection failed.', appdistrack.config.database));
            if (global.config.ENV_DEV) console.log('Error! Database %s connection failed.', appdistrack.config.database);
        });
        db.once('open', function callback() {
            logger.info(util.format('Database %s connection established!', appdistrack.config.database));
            if (global.config.ENV_DEV) console.log('Database %s connection established!', appdistrack.config.database);
        });

        return db;
    }
}

module.exports.appdistrack = appdistrack.createConnection();

// ============================================================
// configuracion conexion -> dpro
// ============================================================

var dpro = {
    config: {
        host: '127.0.0.1',
        port: 27017,
        database: 'dpro',
        user: undefined,
        password: undefined
    },
    createConnection: function() {
        var db = mongoose.createConnection(util.format('mongodb://%s:%s/%s', this.config.host, this.config.port, this.config.database));
        db.on('error', function() {
            logger.error(util.format('Error! Database %s connection failed.', dpro.config.database));
            if (global.config.ENV_DEV) console.log('Error! Database %s connection failed.', dpro.config.database);
        });
        db.once('open', function callback() {
            logger.info(util.format('Database %s connection established!', dpro.config.database));
            if (global.config.ENV_DEV) console.log('Database %s connection established!', dpro.config.database);
        });

        return db;
    }
}

module.exports.dpro = dpro.createConnection();

// ============================================================
// configuracion conexion -> molitalia
// ============================================================

var molitalia = {
    config: {
        host: '127.0.0.1',
        port: 27017,
        database: 'molitalia',
        user: undefined,
        password: undefined
    },
    createConnection: function() {
        var db = mongoose.createConnection(util.format('mongodb://%s:%s/%s', this.config.host, this.config.port, this.config.database));

        db.on('error', function() {
            logger.error(util.format('Error! Database %s connection failed.', molitalia.config.database));
            if (global.config.ENV_DEV) console.log('Error! Database %s connection failed.', molitalia.config.database);
        });
        db.once('open', function callback() {
            logger.info(util.format('Database %s connection established!', molitalia.config.database));
            if (global.config.ENV_DEV) console.log('Database %s connection established!', molitalia.config.database);
        });

        return db;
    }
}

module.exports.molitalia = molitalia.createConnection();