var pedidoModel = require('../models/pedidoModel');

var usuarioController = require('../controllers/usuarioController');
var atributoController = require('../controllers/atributoController');

var functionglobal = require('../common/functions');

module.exports.list = function(params, callback) {
    // params = {idusuario:null, estado:null}
    usuarioController.filter(params.usuario, function(usuario, error) {
        if (error) {
            callback(null, error);
        } else {
            var response = { success: false, data: null };

            if (usuario) {
                atributoController.getAtributos(usuario.bbdd, 'pedidos', function(atributo, error1) {
                    if (error1) {
                        callback(null, error1);
                    } else {
                        if (atributo) {
                            var projection = functionglobal.mongo_getProjection('documento,cliente,idpedido,peso,cantidad,dircliente,aux5', ',');
                            //var projection = functionglobal.mongo_getProjection(atributo.cmp_vista1, ',');

                            pedidoModel.list_by_state(usuario.bbdd, params, projection, function(error2, pedidos) {
                                if (error2) {
                                    callback(null, error2);
                                } else {
                                    if (pedidos) {
                                        response = {
                                            success: true,
                                            data: pedidos
                                        };
                                    }
                                    callback(response, null);
                                }
                            });
                        } else {
                            callback(response, null);
                        }
                    }
                });
            } else {
                callback(response, null);
            }
        }
    });
}

module.exports.getPorDocumento = function(params, callback) {
    pedidoModel.getPorDocumento(params, function(error, data) {
        if (error) {
            callback(null, error);
        } else {
            var response;
            if (data) {
                response = {
                    success: true,
                    pedido: {
                        documento: data.documento,
                        idpedido: data.idpedido,
                        cliente: data.cliente,
                        peso: data.peso,
                        cantidad: data.cantidad,
                        dircliente: data.dircliente,
                        aux5: data.aux5
                    }
                };
            } else {
                response = { success: false, data: null };
            }
            callback(response, null);
        }
    });
}

module.exports.addPedido = function(params, callback) {
    pedidoModel.addPedido(params, function(success) {
        callback(success);
    });
}

module.exports.existPedido = function(params, callback) {
    pedidoModel.existPedido(params, function(success) {
        callback(success);
    });
}

module.exports.updatePedido = function(params, callback) {
    pedidoModel.updatePedido(params, function(success) {
        callback(success);
    });
}

module.exports.updatePedidoFgFinaliza = function(params, callback) {
    pedidoModel.updatePedidoFgFinaliza(params, function(success) {
        callback(success);
    });
}

module.exports.deletePedido = function(callback) {
    pedidoModel.deletePedido(function(success) {
        callback(success);
    });
}