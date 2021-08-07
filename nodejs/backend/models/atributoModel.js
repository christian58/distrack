'use strict';

var appdistrack = require('./collections/appdistrack');

// filtrar atributo por bbdd y tbl_programacion
module.exports.filter = function(_bbdd, _program, done) {
    appdistrack.getAtributoBbdd.findOne({
            bbdd: _bbdd,
            tbl_programacion: _program
        }, {
            _id: true,
            id_atributo: true,
            bbdd: true,
            tbl_programacion: true,
            tbl_estado_motivos: true,
            tbl_estado2: true,
            lbl_estado2: true,
            cmp_vista1: true,
            cmp_vista2: true,
            ruta_bbdd: true,
            puerto_bbdd: true
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

module.exports.deleteAtributoBbdd = function(callback) {
    appdistrack.getAtributoBbdd.remove(function(err, p){
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.addAtributoBbdd = function(params, callback) {
    appdistrack.getAtributoBbdd.create({
        id_atributo: params.id_atributo,
        bbdd: params.bbdd,
        tbl_programacion: params.tbl_programacion,
        tbl_estado_motivos: params.tbl_estado_motivos,
        tbl_estado2: params.tbl_estado2,
        lbl_estado2: params.lbl_estado2,
        cmp_vista1: params.cmp_vista1,
        cmp_vista2: params.cmp_vista2,
        ruta_bbdd: params.ruta_bbdd,
        puerto_bbdd: params.puerto_bbdd
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}