var fs = require('fs')
var mv = require('mv');

module.exports.mongo_getProjection = function(fields, separator) {
    // si los campos son nulos
    if (fields == null)
        return null;

    // si no hay separador
    if (separator == null)
        return null;

    var arrFields = fields.split(separator);
    var result = '';

    if (arrFields) {
        for (var i = 0; i < arrFields.length; i++) {
            var s = arrFields[i].trim();

            if (s.length > 0) {
                result += '"' + arrFields[i].trim() + '": true,'
            }
        }

        result = JSON.parse('{ ' + result.substr(0, result.length - 1) + ' } ');
    } else {
        result = null;
    }

    return result;
}

module.exports.move_rename_file = function(oldPath, newPath, cb) {
    fs.rename(oldPath, newPath, function(err) {
        if (err) cb(err);

        //console.log('Successfully renamed - AKA moved!')
        cb(null);
    });
}

module.exports.movefile = function(source, dest, cb) {
    mv(source, dest, { mkdirp: true }, function(err) {
        if (err) {
            cb(err);
        } else {
            // done. it first created all the necessary directories, and then 
            // tried fs.rename, then falls back to using ncp to copy the dir 
            // to dest and then rimraf to remove the source dir 
            cb(null);
        }
    });
}