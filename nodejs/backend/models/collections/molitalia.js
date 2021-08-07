'use strict';

var cn = require('../../conexion/conexionMongo');
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports.getPedidos = cn.molitalia.model('pedidos',
    new Schema({
        indice: { type: Number, index: true },
        numpedido: { type: String, index: true },
        idpedido: String,
        localpedido: String,
        fechaprog: Date,
        detalle: String,
        cantidad: String,
        volumen: String,
        cliente: String,
        telfcliente: String,
        dircliente: String,
        distcliente: String,
        refcliente: String,
        codproducto: String,
        placa: String,
        ronda: String,
        ventanaini: String,
        ventanafin: String,
        orden: Number,
        fecentrega: String,
        horentrega: String,
        documento: String,
        estado: String,
        latitud: String,
        longitud: String,
        idplaca: String,
        producto: String,
        sede: String,
        peso: String,
        prestaciones: String,
        clase: String,
        nombre: String,
        aux1: String,
        aux2: String,
        aux3: String,
        aux4: String,
        aux5: String,
        aux6: String,
        aux7: String,
        aux8: String,
        aux9: String,
        aux10: String,
        observacion: String,
        aux11: String,
        aux12: String,
        fupdate: String,
        aux13: String,
        aux14: String,
        fot_foto: String,
        u_vendedor: String,
        u_supervisor: String,
        u_transportista: String,
        f_email: String,
        hora_llegada: String,
        cobranza: String
    }),
    'pedidos'
);

module.exports.getEstados = cn.molitalia.model('estados',
    new Schema({
        indice: { type: Number, index: true },
        codigo: String,
        motivos: String,
        estado: String,
        active: String,
        finaliza: String
    }),
    'estados'
);

module.exports.getEstados_2 = cn.molitalia.model('estados_2',
    new Schema({
        indice: { type: Number, index: true },
        estado: String,
        descripcion: String,
        active: String,
        codigo: String
    }),
    'estados_2'
);

module.exports.database = function() {
    return 'molitalia';
}