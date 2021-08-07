var express = require('express');
var router = express.Router();
var logger = require('log4js').getLogger('pedidosRoutes');

var pedidoController = require('../controllers/pedidoController');

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('routes pedidos...');
});

router.get('/getList', function(req, res) {
    var params = {
        usuario: req.query.usuario,
        estado: 'Pendiente' //'Pendiente'
    };
    pedidoController.list(params, function(data, error) {
        if (error) {
            logger.error(error);
            res.sendStatus(500);
        } else {
            res.json(data);
        }
    });
});

router.get('/buscar-por-documento', function(req, res) {
    var params = {
        documento: req.query.documento
    };
    pedidoController.getPorDocumento(params, function(data, error) {
        if (error) {
            logger.error(error);
            res.sendStatus(500);
        } else {
            res.json(data);
        }
    });
});

module.exports = router;