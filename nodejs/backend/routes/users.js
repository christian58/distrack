var express = require('express');
var router = express.Router();
var logger = require('log4js').getLogger('usersRoutes');
var usuarioController = require('../controllers/usuarioController');
var usuario = require('../domain/usuario');

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

router.post('/login', function(req, res) {
    var params = {
        usuario: req.body.usuario,
        password: req.body.password,
        latitud: req.body.latitud,
        longitud: req.body.longitud,
        tipo: 1
    }

    usuarioController.login(params, function(data, error) {
        if (error) {
            logger.error(error);
            res.sendStatus(500);
        } else {
            res.json(data);
        }
    });
});

router.post('/logout', function(req, res) {
    var params = {
        idusuario: req.body.idusuario,
        usuario: req.body.usuario,
        latitud: req.body.latitud,
        longitud: req.body.longitud,
        tipo: 0
    }

    usuarioController.logout(params, function(data) {
        res.json({ success: data });
    });
});

module.exports = router;