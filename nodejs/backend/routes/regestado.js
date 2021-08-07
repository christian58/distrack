var express = require('express');
var router = express.Router();
var async = require('async');
var dateFormat = require('dateformat');
var regestadoController = require('../controllers/regestadoController');
var pedidoController = require('../controllers/pedidoController');

router.get('/', function(req, res, next) {
    res.send('routes pedidos...');
});

router.post('/update', function(req, res) {
	//try {
	   	if(req.body.data.length) {
	   		var data = req.body.data;
	   		var response = [];

            async.each(data, function(item, done) {
	   		    var params = {
			        documento: item.documento,
			        idpedido: item.idpedido,
			        usuario: item.usuario,
			        usuario_id: item.usuario_id,
			        estado: item.estado,
			        motivo: item.motivo,
			        cobranza: item.cobranza,
			        fecha: item.fecha,
			        latitud: item.latitud,
			        longitud: item.longitud,
			        comentario: item.comentario,
			        cod_estado: item.cod_estado,
			        cod_motivo: item.cod_motivo,
			        cod_cobranza: item.cod_cobranza
			    }

			    if(item.cod_cobranza != "") {
			    	params.fg_finaliza = true;
					  pedidoController.updatePedidoFgFinaliza(params, function(success) {});
          		}

				params.fecha__ = dateFormat(params.fecha, "yyyy-mm-dd");
    			params.hora = dateFormat(params.fecha, "H:MM:ss");

			    regestadoController.updateEstadoMYSQL(params, function(data) {});

			    regestadoController.updateEstado(params, function(data) {
                    response.push({documento: item.documento, success: data});
                    if(data) {
                    	done();
                    } else {
                    	done(data);
                    }
                });

            }, function(err) {
                res.json(response);
            });
	   	} else {
	    	res.json({success: false, data: null});
	   	}
	//} catch (e) {
	//    res.json({success: false, data: null});
	//}
});

module.exports = router;
