var regestadoModel = require('../models/regestadoModel');

module.exports.updateEstado = function(params, callback) {
    regestadoModel.updateEstado(params, function(success) {
        if(success) {
        	callback(true);
        } else {
            callback(false);
        }
    });
}

module.exports.updateEstadoMYSQL = function(params, callback) {
    regestadoModel.updateEstadoMYSQL(params, function(success) {
        if(success) {
        	callback(true);
        } else {
            callback(false);
        }
    });
}