var fs = require('fs');
var mv = require('mv');
var mime = require('mime-types');
var imagesize = require('imagesize');
var uploadfileModel = require('../models/uploadfileModel');
var functionglobal = require('../common/functions');

const MAXIMUN_SIZE = 512000;
const FOLDER_PEDIDO = 'uploads/pedido/';

module.exports.store = function(uploadfile, callback) {
    uploadfileModel.store(uploadfile, function(error) {
        if (error) {
            callback(null, error);
        } else {
            callback({ success: 1, message: 'Registrado con exito.' }, null);
        }
    });
}

module.exports.validate = function(req, folder, folderComplete, name, callback) {
    var file = req.file;
    var newname = name + '.' + mime.extension(file.mimetype);
    var oldPath = file.path;
    var newPath = folderComplete + newname;

    var rutaPublic = folder + newname;

    // validación del tamañao
    if (file.size <= MAXIMUN_SIZE) {
        // grabar correctamente
        functionglobal.movefile(oldPath, newPath, function(err) {
            if (err) {
                callback(err, null);
            } else {
                callback(null, rutaPublic);
            }
        });
    } else {
        // disminuir el tamaño como maximo 500KB
        var fileStream = fs.createReadStream(file.path);

        imagesize(fileStream, function(err, result) {
            if (!err) {
                console.log(result); // {type, width, height}
                callback(null, rutaPublic);
            }
        });
        callback(null, rutaPublic);
    }
}