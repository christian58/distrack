'use strict';

var cn = require('../../conexion/conexionMongo');
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports.getUsuarios = cn.appdistrack.model('usuarios',
    new Schema({
        idusuarios: { type: Number, index: true },
        usuario: { type: String, index: true },
        password: String,
        completo: String,
        max_fotos: String,
        organizacion: String,
        bbdd: String,
        sesion: Number
    }),
    'usuarios'
);

module.exports.getLinkBbdd = cn.appdistrack.model('link_bbdd',
    new Schema({
        idlink_tabla: Number,
        usuario: String,
        bbdd: String,
        ruta_bbdd: String,
        tbl_programacion: String,
        tbl_estados_motivos: String,
        tbl_estados2: String //,
            //usuario_id: [{ type: Schema.Types.ObjectId, ref: 'usuarios' }]
    }),
    'link_bbdd'
);

module.exports.getAtributoBbdd = cn.appdistrack.model('atributo_bbdd',
    new Schema({
        id_atributo: Number,
        bbdd: String,
        tbl_programacion: String,
        tbl_estado_motivos: String,
        tbl_estado2: String,
        lbl_estado2: String,
        cmp_vista1: String,
        cmp_vista2: String,
        ruta_bbdd: String,
        puerto_bbdd: String
    }),
    'atributo_bbdd'
);

module.exports.getRegIngresos = cn.appdistrack.model('reg_ingresos',
    new Schema({
        idusuario: Number,
        usuario: String,
        //usuario_id: [{ type: Schema.Types.ObjectId, ref: 'usuarios' }],
        fecha: { type: Date, default: Date.now },
        latitud: String,
        longitud: String,
        tipo: String
    }),
    'reg_ingresos'
);

module.exports.getUploadFiles = cn.appdistrack.model('upload_files',
    new Schema({
        //idupload: { type: Number, index: true },
        idusuario: { type: Number, index: true },
        usuario: String,
        idRelation: { type: String, index: true }, // idpedido
        idtable: Number,
        path: String,
        destination: String,
        filename: String,
        size: Number,
        created: { type: Date, default: Date.now }
    }),
    'upload_files'
);

module.exports.getRegEstados = cn.appdistrack.model('reg_estados',
    new Schema({
        documento: String,
        idusuario: Number,
        usuario: String,
        //usuario_id: [{ type: Schema.Types.ObjectId, ref: 'usuarios' }],
        estado: String,
        motivo: String,
        cobranza: String,
        fecha: { type: Date },
        latitud: String,
        longitud: String,
        comentario: String,
        cod_estado: String,
        cod_motivo: String,
        cod_cobranza: String
    }),
    'reg_estados'
);

module.exports.getRegFotos = cn.appdistrack.model('reg_fotos',
    new Schema({
        idusuario: Number,
        idpedido: Number,
        documento: String,
        usuario: String,
        //usuario_id: [{ type: Schema.Types.ObjectId, ref: 'usuarios' }],
        ruta: String,
        estado: String,
        fecha: String,
        hora: String,
        latitud: String,
        longitud: String,
        sincronizado: { type: Boolean, default: false }
    }),
    'reg_fotos'
);

module.exports.getRegFirmas = cn.appdistrack.model('reg_firmas',
    new Schema({
        idusuario: Number,
        indice: Number,
        documento: String,
        usuario: String,
        //usuario_id: [{ type: Schema.Types.ObjectId, ref: 'usuarios' }],
        ruta: String,
        estado: String,
        fecha: String,
        hora: String,
        latitud: String,
        longitud: String
    }),
    'reg_firmas'
);

// [START MOLITALIA]

module.exports.getEstados_Molitalia = cn.appdistrack.model('estados_molitalia',
    new Schema({
        codigo: { type: String, index: true },
        motivos: String,
        estado: String,
        active: String,
        finaliza: String
    }),
    'estados_molitalia'
);

module.exports.getEstados2_Molitalia = cn.appdistrack.model('estados2_molitalia',
    new Schema({
        estado: String,
        descripcion: String,
        active: String,
        codigo: { type: String, index: true }
    }),
    'estados2_molitalia'
);

module.exports.getPedidos_Molitalia = cn.appdistrack.model('pedidos_molitalia',
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
        cobranza: String,
        fh_update: Date,
        fg_finaliza: { type: Boolean, default: false}
    }),
    'pedidos_molitalia'
);

// [END MOLITALIA]