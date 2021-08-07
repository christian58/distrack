var express = require('express');
var router = express.Router();
var multer = require('multer');
var path = require('path');
var mkdirp = require('mkdirp');
var logger = require('log4js').getLogger('uploadfileRoutes');
var dateFormat = require('dateformat');

var customStorage = require('../template/customStorage');
var uploadfileController = require('../controllers/uploadfileController');
var regfotoController = require('../controllers/regfotoController');
var regfirmaController = require('../controllers/regfirmaController');

const FOLDER_BASE = './tracklog-distrack-backend/backend/files/';
const FOLDER_DESTINATION = FOLDER_BASE + 'uploads/originalsize/';
const FOLDER_PEDIDO = 'uploads/pedido/';
const FOLDER_PEDIDO_COMPLETE = FOLDER_BASE + FOLDER_PEDIDO;
const FOLDER_FIRMA = 'uploads/firma/';
const FOLDER_FIRMA_COMPLETE = FOLDER_BASE + FOLDER_FIRMA;

var upload = multer({
    fileFilter: function(req, file, cb) {
        if (!file.originalname.match(/\.(jpg|jpeg|png|PNG|gif)$/)) {
            return cb(new Error('Only image files are allowed!'));
        }
        cb(null, true);
    },
    storage: customStorage({
        destination: function(req, file, cb) {
            mkdirp.sync(FOLDER_DESTINATION);
            cb(null, FOLDER_DESTINATION);
        }
    })
}).single('fileImage');

/* GET users listing. */
router.get('/', function(req, res, next) {
    res.send('respond with a resource');
});

router.post('/foto', function(req, res) {
    upload(req, res, function(err) {
        if (err) {
            //return res.end("Something went wrong!");
            logger.error(err);
            return res.json({ success: false });
        }

        if (req.body) {
            var now = new Date();
            var newname = req.body.idusuario + '_' + req.body.idpedido + '_' + dateFormat(now, "yyyymmdd-hMMss");
            uploadfileController.validate(req, FOLDER_PEDIDO, FOLDER_PEDIDO_COMPLETE, newname, function(error, ruta) {
                if (error) {
                    logger.error(err);
                    return res.json({ success: false });
                } else {
                    //console.log(req.body);

                    var params = {
                        idusuario: Number(req.body.idusuario),
                        idpedido: Number(req.body.idpedido),
                        documento: req.body.documento,
                        // usuario: isArray(req.body.usuario) ? req.body.usuario[0] : usuario,
                        usuario: req.body.usuario,
                        ruta: ruta,
                        estado: req.body.estado,
                        fecha: req.body.fecha,
                        hora: req.body.hora,
                        latitud: req.body.latitud,
                        longitud: req.body.longitud
                    };

                    regfotoController.addRegFotoMYSQL(params, function(success) {
                        if(success) {
                            console.log(true);
                        } else {
                            console.log(false);
                        }
                    });

                    regfotoController.addRegFoto(params, function(success) {
                        if (!success) {
                            //return res.end("Something went wrong!");
                            res.json({ success: false });
                        } else {
                            //return res.end("File uploaded sucessfully!.");
                            return res.json({ success: true });
                        }
                    });
                }
            });
        } else {
            return res.json({ success: false });
        }
    });
});

router.post('/firma', function(req, res) {
    upload(req, res, function(err) {
        if (err) {
            logger.error(err);
            return res.json({ success: false });
        } else {

            if (req.body) {
                var now = new Date();
                var newname = req.body.idusuario + '_' + dateFormat(now, "yyyymmdd-hMMss");

                uploadfileController.validate(req, FOLDER_FIRMA, FOLDER_FIRMA_COMPLETE, newname, function(error, ruta) {
                    if (error) {
                        logger.error(err);
                        return res.json({ success: false });
                    } else {
                        var params = {
                            idusuario: Number(req.body.idusuario),
                            documento: req.body.documento,
                            usuario: req.body.usuario,
                            ruta: ruta,
                            estado: req.body.estado,
                            fecha: req.body.fecha,
                            hora: req.body.hora,
                            latitud: req.body.latitud,
                            longitud: req.body.longitud
                        };

                        regfirmaController.addRegFirmaMYSQL(params, function(success) {
                            if(success) {
                                console.log(true);
                            } else {
                                console.log(false);
                            }
                        });

                        regfirmaController.addRegFirma(params, function(success) {
                            if (!success) {
                                //return res.end("Something went wrong!");
                                res.json({ success: false });
                            } else {
                                //return res.end("File uploaded sucessfully!.");
                                return res.json({ success: true });
                            }
                        });
                    }
                });

            } else {
                return res.json({ success: false });
            }
        }
    });
});

function isArray(myArray) {
    return myArray.constructor.toString().indexOf("Array") > -1;
}

module.exports = router;
