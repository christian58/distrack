var syncModel = require('../models/syncModel');

module.exports.getUsuarios = function(callback) {
    syncModel.getUsuarios(function(error, data) {
        if(data) {
        	callback(null, data);
        } else {
            callback(error, null);
        }
    });
}

module.exports.getLinkBbdd = function(callback) {
    syncModel.getLinkBbdd(function(error, data) {
        if(data) {
        	callback(null, data);
        } else {
            callback(error, null);
        }
    });
}

module.exports.getAtributoBbdd = function(callback) {
    syncModel.getAtributoBbdd(function(error, data) {
        if(data) {
        	callback(null, data);
        } else {
            callback(error, null);
        }
    });
}

module.exports.getEstadosMolitalia = function(callback) {
    syncModel.getEstadosMolitalia(function(error, data) {
        if(data) {
            callback(null, data);
        } else {
            callback(error, null);
        }
    });
}

module.exports.getEstados2Molitalia = function(callback) {
    syncModel.getEstados2Molitalia(function(error, data) {
        if(data) {
            callback(null, data);
        } else {
            callback(error, null);
        }
    });
}

module.exports.getPedidosPendientesMolitalia = function(callback) {
    syncModel.getPedidosPendientesMolitalia(function(error, data) {
        if(data) {
            callback(null, data);
        } else {
            callback(error, null);
        }
    });
}