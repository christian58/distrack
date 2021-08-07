var linkbbddModel = require('../models/linkbbddModel');

module.exports.deleteLinkbbdd = function(callback) {
    linkbbddModel.deleteLinkbbdd(function(success) {
        callback(success);
    });
}

module.exports.addLinkbbdd = function(params, callback) {
    linkbbddModel.addLinkbbdd(params, function(success) {
        callback(success);
    });
}