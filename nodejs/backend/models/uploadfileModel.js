'use strict';

var appdistrack = require('./collections/appdistrack');

module.exports.store = function(uploadfile, done) {
    appdistrack.getUploadFiles.create({
            idusuario: uploadfile.idusuario,
            idRelation: uploadfile.idRelation,
            idtable: uploadfile.idtable,
            path: uploadfile.path,
            destination: uploadfile.destination,
            filename: uploadfile.filename,
            size: uploadfile.size
        },
        function(err, rowCreate) {
            if (err) {
                done(err);
            } else {
                done(null);
            }
        }
    );
}