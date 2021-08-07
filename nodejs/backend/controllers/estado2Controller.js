var estado2Model = require('../models/estado2Model');

module.exports.list_2 = function(callback) {
    var active = '1';

    estado2Model.list_2(active, function(error, estados) {
        if (error) {
            callback(null, error);
        } else {
            var response = { success: false, data: null };

            if (estados) {
                var data = [];
                // cargar estados de cobranza
                estados.forEach(function(item) {
                    data.push({
                        estado: item.estado,
                        descripcion: item.descripcion,
                        codigo: item.codigo
                    });
                });

                response.success = true;
                response.data = data;

                callback(response, null);
            } else {
                callback(response, null);
            }
        }
    });
}

module.exports.deleteEstados2 = function(callback) {
    estado2Model.deleteEstados2(function(success) {
        callback(success);
    });
}

module.exports.addEstados2 = function(params, callback) {
    estado2Model.addEstados2(params, function(success) {
        callback(success);
    });
}