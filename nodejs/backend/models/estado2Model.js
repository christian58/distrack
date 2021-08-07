'use strict';

var appdistrack = require('./collections/appdistrack');

module.exports.list_2 = function(active, done) {
    appdistrack.getEstados2_Molitalia.find({
            active: active
        }, {
            indice: true,
            estado: true,
            descripcion: true,
            active: true,
            codigo: true
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
        descripcion: 1
    });
}

module.exports.deleteEstados2 = function(callback) {
    appdistrack.getEstados2_Molitalia.remove(function(err, p){
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.addEstados2 = function(params, callback) {
    appdistrack.getEstados2_Molitalia.create({
        estado: params.estado,
        descripcion: params.descripcion,
        active: params.active,
        codigo: params.codigo
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}