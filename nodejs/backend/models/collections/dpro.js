'use strict';

var cn = require('../../conexion/conexionMongo');
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports.getPedidos = cn.dpro.model('pedidos',
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
        clasificacion: String,
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
        programado: String,
        observacion: String,
        clase: String,
        aux1: String,
        aux2: String,
        aux3: String,
        peso: String,
        fot_foto: String,
        gui_foto: String,
        route_id: String,
        sku: String,
        dni: String,
        f_email: String,
        aux4: String,
        fecreprog: String
    }),
    'pedidos'
);

module.exports.database = function() {
    return 'dpro';
}