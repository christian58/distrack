var estadoModel = require('../models/estadoModel');
var async = require('async');
var arraySort = require('array-sort');

// listar estados
module.exports.list = function(callback) {
    var active = '1';
    var finaliza = '1';

    // select distinct estados
    estadoModel.get_estados(function(error, estados) {
        if (error) {
            callback(null, error);
        } else {
            var response = { success: false, data: [] };
            if (estados) {
                var data = [];
                async.each(estados, function(item, done) {
                    //console.log(item);
                    estadoModel.get_motivos(item, active, finaliza, function(error1, motivos) {
                        if (error1) {
                            done(error1);
                        } else {
                            if (motivos) {
                                if (motivos.length == 0) {
                                    data.push({ estado: item, motivos: [] });
                                    done();
                                } else {
                                    var datarow = { estado: item, motivos: [] };

                                    // cargar motivos
                                    motivos.forEach(function(motivo) {
                                        datarow.motivos.push({
                                            label: motivo.motivos
                                            , codigo: motivo.codigo
                                            , finaliza: motivo.finaliza
                                        });
                                    });

                                    // cargar datarow in data
                                    data.push(datarow);

                                    done();
                                }
                            } else {
                                data.push({ estado: item, motivos: null });
                            }
                        }
                    });

                }, function(err) {
                    if (err) {
                        callback(null, err);
                    } else {
                        response.success = true;
                        response.data = arraySort(data, 'estado');

                        callback(response, null);
                    }
                });

            } else {
                callback(response, null);
            }
        }
    });
}

module.exports.deleteEstados = function(callback) {
    estadoModel.deleteEstados(function(success) {
        callback(success);
    });
}

module.exports.addEstados = function(params, callback) {
    estadoModel.addEstados(params, function(success) {
        callback(success);
    });
}
