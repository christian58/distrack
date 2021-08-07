'use strict';

var appdistrack = require('./collections/appdistrack');
var conMySQL = require('../conexion/conexionMySQL');

module.exports.addRegFirma = function(params, callback) {
    appdistrack.getRegFirmas.create({
        idusuario: params.idusuario,
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

module.exports.addRegFirmaMYSQL = function(params, callback) {
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
    conMySQL.appdistrack_mysql2.query('INSERT INTO appdistrack.reg_firmas SET ?', post, function(err, result) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}