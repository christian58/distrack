'use strict';

var appdistrack = require('./collections/appdistrack');
var conMySQL = require('../conexion/conexionMySQL');

module.exports.login = function(params, done) {
    appdistrack.getUsuarios.findOne({
            usuario: params.usuario,
            password: params.password
        }, {
            _id: true,
            idusuarios: true,
            usuario: true,
            completo: true,
            max_fotos: true,
            organizacion: true
        },
        function(err, row) {
            if (err) {
                done(err, null);
            } else {
                done(null, row);
            }
        }
    );
}

module.exports.filter = function(usuario, done) {
    appdistrack.getUsuarios.findOne({
            usuario: usuario
        }, {
            _id: true,
            usuario: true,
            bbdd: true
        },
        function(err, row) {
            if (err) {
                done(err, null);
            } else {
                done(null, row);
            }
        }
    );
}

module.exports.setRegIngreso = function(params, callback) {
    appdistrack.getRegIngresos.create({
        usuario: params.usuario,
        latitud: params.latitud,
        longitud: params.longitud,
        tipo: params.tipo
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.setSesion = function(params, callback) {
    appdistrack.getUsuarios.update({usuario: params.usuario}, {
        sesion: params.sesion
    }, function(err, affected, resp) {
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.deleteUsuario = function(callback) {
    appdistrack.getUsuarios.remove(function(err, p){
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.addUsuario = function(params, callback) {
    appdistrack.getUsuarios.create({
        idusuarios: params.idusuarios,
        usuario: params.usuario,
        password: params.password,
        completo: params.completo,
        max_fotos: params.max_fotos,
        organizacion: params.organizacion,
        bbdd: params.bbdd,
        sesion: params.sesion
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.setRegIngresoMYSQL = function(params, callback) {
    var post  = {
        usuario: params.usuario,
        fecha: params.fecha,
        hora: params.hora,
        latitud: params.latitud,
        longitud: params.longitud,
        tipo: params.tipo,
    };
    conMySQL.appdistrack_mysql2.query('INSERT INTO appdistrack.reg_ingresos SET ?', post, function(err, result) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}