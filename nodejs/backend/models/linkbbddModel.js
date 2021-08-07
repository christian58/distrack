'use strict';

var appdistrack = require('./collections/appdistrack');

module.exports.deleteLinkbbdd = function(callback) {
    appdistrack.getLinkBbdd.remove(function(err, p){
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.addLinkbbdd = function(params, callback) {
    appdistrack.getLinkBbdd.create({
        idlink_tabla: params.idlink_tabla,
        usuario: params.usuario,
        bbdd: params.bbdd,
        ruta_bbdd: params.ruta_bbdd,
        tbl_programacion: params.tbl_programacion,
        tbl_estados_motivos: params.tbl_estados_motivos,
        tbl_estados2: params.tbl_estados2
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}