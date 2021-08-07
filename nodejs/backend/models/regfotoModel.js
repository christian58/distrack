'use strict';

var appdistrack = require('./collections/appdistrack');
var conMySQL = require('../conexion/conexionMySQL');

module.exports.addRegFoto = function(params, callback) {
    appdistrack.getRegFotos.create({
        idusuario: params.idusuario,
        idpedido: params.idpedido,
        documento: params.documento,
        usuario: params.usuario,
        ruta: params.ruta,
        estado: params.estado,
        fecha: params.fecha,
        hora: params.hora,
        latitud: params.latitud,
        longitud: params.longitud
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.addRegFotoMYSQL = function(params, callback) {
    var post  = {
        documento: params.documento,
        usuario: params.usuario,
        ruta: params.ruta,
        estado: params.estado,
        fecha: params.fecha,
        hora: params.hora,
        latitud: params.latitud,
        longitud: params.longitud
    };
    conMySQL.appdistrack_mysql2.query('INSERT INTO appdistrack.reg_fotos SET ?', post, function(err, result) {
        if (err) {
            console.log(err);
            callback(false);
        } else {
            callback(true);
        }
    });
}