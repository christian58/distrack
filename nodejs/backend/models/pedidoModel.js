'use strict';

var appdistrack = require('./collections/appdistrack');

module.exports.list_by_state = function(bbdd, params, projection, done) {
    appdistrack.getPedidos_Molitalia.find({
            u_transportista: params.usuario,
            fg_finaliza: false
        },
        projection,
        function(err, rows) {
            if (err) {
                done(err, null);
            } else {
            done(null, rows);
            }
        }
    );
}

module.exports.getPorDocumento = function(params, done) {
    appdistrack.getPedidos_Molitalia.findOne({
            documento: params.documento
        },
        {
            documento: true,
            idpedido: true,
            cliente: true,
            peso: true,
            cantidad: true,
            dircliente: true,
            aux5: true
        },
        function(err, rows) {
            if (err) {
                done(err, null);
            } else {
                done(null, rows);
            }
        }
    );
}

module.exports.addPedido = function(params, callback) {
    appdistrack.getPedidos_Molitalia.create({
        indice: params.indice,
        numpedido: params.numpedido,
        idpedido: params.idpedido,
        localpedido: params.localpedido,
        fechaprog: params.fechaprog,
        detalle: params.detalle,
        cantidad: params.cantidad,
        volumen: params.volumen,
        cliente: params.cliente,
        telfcliente: params.telfcliente,
        dircliente: params.dircliente,
        distcliente: params.distcliente,
        refcliente: params.refcliente,
        codproducto: params.codproducto,
        placa: params.placa,
        ronda: params.ronda,
        ventanaini: params.ventanaini,
        ventanafin: params.ventanafin,
        orden: params.orden,
        fecentrega: params.fecentrega,
        horentrega: params.horentrega,
        documento: params.documento,
        estado: params.estado,
        latitud: params.latitud,
        longitud: params.longitud,
        idplaca: params.idplaca,
        producto: params.producto,
        sede: params.sede,
        peso: params.peso,
        prestaciones: params.prestaciones,
        clase: params.clase,
        nombre: params.nombre,
        aux1: params.aux1,
        aux2: params.aux2,
        aux3: params.aux3,
        aux4: params.aux4,
        aux5: params.aux5,
        aux6: params.aux6,
        aux7: params.aux7,
        aux8: params.aux8,
        aux9: params.aux9,
        aux10: params.aux10,
        observacion: params.observacion,
        aux11: params.aux11,
        aux12: params.aux12,
        fupdate: params.fupdate,
        aux13: params.aux13,
        aux14: params.aux14,
        fot_foto: params.fot_foto,
        u_vendedor: params.u_vendedor,
        u_supervisor: params.u_supervisor,
        u_transportista: params.u_transportista,
        f_email: params.f_email,
        hora_llegada: params.hora_llegada,
        cobranza: params.cobranza,
        fh_update: params.fh_update,
        fg_finaliza: params.fg_finaliza
    }, function(err, row) {
        if (err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.existPedido = function(params, done) {
    appdistrack.getPedidos_Molitalia.findOne({
            indice: params.indice
        }, {
            indice: true
        },
        function(err, rows) {
            if (err) {
                done(false);
            } else {
                if(rows) {
                    done(true);
                } else {
                    done(false);
                }   
            }
        }
    );
}

module.exports.updatePedido = function(params, callback) {
    appdistrack.getPedidos_Molitalia.update({indice: params.indice}, {
        numpedido: params.numpedido,
        idpedido: params.idpedido,
        localpedido: params.localpedido,
        fechaprog: params.fechaprog,
        detalle: params.detalle,
        cantidad: params.cantidad,
        volumen: params.volumen,
        cliente: params.cliente,
        telfcliente: params.telfcliente,
        dircliente: params.dircliente,
        distcliente: params.distcliente,
        refcliente: params.refcliente,
        codproducto: params.codproducto,
        placa: params.placa,
        ronda: params.ronda,
        ventanaini: params.ventanaini,
        ventanafin: params.ventanafin,
        orden: params.orden,
        fecentrega: params.fecentrega,
        horentrega: params.horentrega,
        documento: params.documento,
        estado: params.estado,
        latitud: params.latitud,
        longitud: params.longitud,
        idplaca: params.idplaca,
        producto: params.producto,
        sede: params.sede,
        peso: params.peso,
        prestaciones: params.prestaciones,
        clase: params.clase,
        nombre: params.nombre,
        aux1: params.aux1,
        aux2: params.aux2,
        aux3: params.aux3,
        aux4: params.aux4,
        aux5: params.aux5,
        aux6: params.aux6,
        aux7: params.aux7,
        aux8: params.aux8,
        aux9: params.aux9,
        aux10: params.aux10,
        observacion: params.observacion,
        aux11: params.aux11,
        aux12: params.aux12,
        fupdate: params.fupdate,
        aux13: params.aux13,
        aux14: params.aux14,
        fot_foto: params.fot_foto,
        u_vendedor: params.u_vendedor,
        u_supervisor: params.u_supervisor,
        u_transportista: params.u_transportista,
        f_email: params.f_email,
        hora_llegada: params.hora_llegada,
        cobranza: params.cobranza,
        fh_update: params.fh_update
    }, function(err, affected, resp) {
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.updatePedidoFgFinaliza = function(params, callback) {
    appdistrack.getPedidos_Molitalia.update({documento: params.documento}, {
        estado: params.estado,
        fg_finaliza: params.fg_finaliza
    }, function(err, affected, resp) {
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}

module.exports.deletePedido = function(callback) {
    appdistrack.getPedidos_Molitalia.remove(function(err, p){
        if(err) {
            callback(false);
        } else {
            callback(true);
        }
    });
}