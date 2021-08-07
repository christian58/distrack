var express = require('express');
var router = express.Router();
var logger = require('log4js').getLogger('estadosRoutes');

var estadoController = require('../controllers/estadoController');

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('routes estados...');
});

router.get('/getList', function(req, res) {
    estadoController.list(function(data, error) {
        if (error) {
            logger.error(error);
            res.sendStatus(500);
        } else {
            res.json(data);
        }
    });
});

module.exports = router;