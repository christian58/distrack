'use strict';

var appdistrack = require('./collections/appdistrack');

module.exports.list = function(active, finaliza, done) {
    appdistrack.getEstados_Molitalia.find({
            active: active,
            finaliza: finaliza
        }, {
            indice: true,
            codigo: true,
            motivos: true,
            estado: true,
            active: true,
            finaliza: true
        },
        function(err, rows) {
            if (err) {
                done(err, null);
            } else {
                done(null, rows);
            }
        }
    ).sort({
        estado: 1,
        motivos: 1
    });
}

module.exports.get_estados = function(done) {
    appdistrack.getEstados_Molitalia.distinct(
        'estado', {
            active: 1
            //, finaliza: 1
        },
        function(err, rows) {
            if (err) {
                done(err, null);
            } else {
                done(null, rows);
            }
        }
    ).sort();
}

module.exports.get_motivos = function(estado, active, finaliza, done) {
    appdistrack.getEstados_Molitalia.find({
            estado: estado
            , active: active
            // , finaliza: finaliza
        }, {
            codigo: true
            , motivos: true
            , finaliza: true
        },
        function(err, rows) {
            if (err) {
                done(err, null);
            } else {
                done(null, rows);
            }
        }
    ).sort({
        codigo: 1
    });
}

module.exports.deleteEstados = function(callback) {
    appdistrack.getEstados_Molitalia.remove(function(err, p){
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.addEstados = function(params, callback) {
    appdistrack.getEstados_Molitalia.create({
        codigo: params.codigo,
        motivos: params.motivos,
        estado: params.estado,
        active: params.active,
        finaliza: params.finaliza
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}
