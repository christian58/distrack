'use strict';

var appdistrack = require('./collections/appdistrack');
var conMySQL = require('../conexion/conexionMySQL');

module.exports.updateEstado = function(params, callback) {
    appdistrack.getRegEstados.create({
        documento: params.documento,
        usuario: params.usuario,
        usuario_id: params.usuario_id,
        estado: params.estado,
        motivo: params.motivo,
        cobranza: params.cobranza,
        fecha: params.fecha,
        latitud: params.latitud,
        longitud: params.longitud,
        comentario: params.comentario,
        cod_estado: params.cod_estado,
        cod_motivo: params.cod_motivo,
        cod_cobranza: params.cod_cobranza
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.updateEstadoMYSQL = function(params, callback) {
    var post  = {
        documento: params.documento,
        usuario: params.usuario,
        estado: params.estado,
        motivo: params.motivo,
        cobranza: params.cobranza,
        fecha: params.fecha__,
        hora: params.hora,
        latitud: params.latitud,
        longitud: params.longitud,
        comentario: params.comentario,
        cod_estado: params.cod_estado,
        cod_motivo: params.cod_motivo,
        cod_cobranza: params.cod_cobranza
    };
    conMySQL.appdistrack_mysql2.query('INSERT INTO appdistrack.reg_estados SET ?', post, function(err, result) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}