var dateFormat = require('dateformat');

var usuarioModel = require('../models/usuarioModel');

module.exports.store = function(usuario, callback) {
    usuarioModel.store(usuario, function(error) {
        if (error) {
            callback(null, error);
        } else {
            callback({ success: 1, message: 'Registrado con exito.' }, error);
        }
    });
}

module.exports.login = function(params, callback) {
    usuarioModel.login(params, function(error, data) {
        if (error) {
            callback(null, error);
        } else {
            var response;
            if (data) {
                params.usuario = data.usuario;

                setIngresoMYSQL(params);

                // [START Set RegIngreso]
                usuarioModel.setRegIngreso(params, function(success) {});
                // [END  Set RegIngreso]

                // [STAR Update Sesion]
                params.sesion = 1;
                usuarioModel.setSesion(params, function(success) {});
                // [END Update Sesion]

                response = {
                    success: true,
                    user: {
                        id:data._id,
                        idusuario: data.idusuarios,
                        usuario: data.usuario,
                        nombre: data.completo,
                        max_foto: new Number(data.max_fotos),
                        organizacion: data.organizacion
                    }
                };
            } else {
                response = { success: false, user: null };
            }
            callback(response, null);
        }
    });
}

module.exports.filter = function(usuario, callback) {
    usuarioModel.filter(usuario, function(error, data) {
        if (error) {
            callback(null, error);
        } else {
            callback(data, null);
        }
    });
}

module.exports.logout = function(params, callback) {
    setIngresoMYSQL(params);
    usuarioModel.setRegIngreso(params, function(successRI) {
        if(successRI) {
            params.sesion = 0;
            usuarioModel.setSesion(params, function(successSE) {
                callback(successSE);
            });
        } else {
            callback(false);
        }
    });
}

module.exports.deleteUsuario = function(callback) {
    usuarioModel.deleteUsuario(function(success) {
        callback(success);
    });
}

module.exports.addUsuario = function(params, callback) {
    usuarioModel.addUsuario(params, function(success) {
        callback(success);
    });
}

function setIngresoMYSQL(params) {
    var now = new Date();
    params.fecha = dateFormat(now, "yyyy-mm-dd");
    params.hora = dateFormat(now, "h:MM:ss");

    usuarioModel.setRegIngresoMYSQL(params, function(success) {});
}
