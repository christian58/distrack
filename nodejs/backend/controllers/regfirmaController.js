'use strict';

var regfirmaModel = require('../models/regfirmaModel');

module.exports.addRegFirma = function(params, callback) {
    regfirmaModel.addRegFirma(params, function(success) {
        callback(success);
    });
}

module.exports.addRegFirmaMYSQL = function(params, callback) {
    regfirmaModel.addRegFirmaMYSQL(params, function(success) {
        callback(success);
    });
}