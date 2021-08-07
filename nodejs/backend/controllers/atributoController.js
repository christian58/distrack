var atributoModel = require('../models/atributoModel');

// obtener atributos por bbdd y tbl_programacion
module.exports.getAtributos = function(bbdd, program, callback) {
    atributoModel.filter(bbdd, program, function(error, data) {
        if (error) {
            callback(null, error);
        } else {
            callback(data, null);
        }
    });
}

module.exports.deleteAtributoBbdd = function(callback) {
    atributoModel.deleteAtributoBbdd(function(success) {
        callback(success);
    });
}

module.exports.addAtributoBbdd = function(params, callback) {
    atributoModel.addAtributoBbdd(params, function(success) {
        callback(success);
    });
}