var express = require('express');
var router = express.Router();
var logger = require('log4js').getLogger('estadosRoutes');

var estado2Controller = require('../controllers/estado2Controller');

router.get('/estado/getList', function(req, res) {
    estado2Controller.list_2(function(data, error) {
        if (error) {
            logger.error(error);
            res.sendStatus(500);
        } else {
            res.json(data);
        }
    });
});

module.exports = router;