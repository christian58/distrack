'use strict';

var regfotoModel = require('../models/regfotoModel');

module.exports.addRegFoto = function(params, callback) {
    regfotoModel.addRegFoto(params, function(success) {
        callback(success);
    });
}

module.exports.addRegFotoMYSQL = function(params, callback) {
    regfotoModel.addRegFotoMYSQL(params, function(success) {
        callback(success);
    });
}