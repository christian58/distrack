'use strict';

var conMySQL = require('../conexion/conexionMySQL');

module.exports.getUsuarios = function(callback) {
    conMySQL.appdistrack_mysql2.execute('SELECT * FROM appdistrack.usuarios', [],
  	function(err, results, fields) {
    	if(err) {
    		callback(err, null);
    	} else {
    		callback(null, results);
    	}
  	});
}

module.exports.getLinkBbdd = function(callback) {
    conMySQL.appdistrack_mysql2.execute('SELECT * FROM appdistrack.link_bbdd', [],
  	function(err, results, fields) {
    	if(err) {
    		callback(err, null);
    	} else {
    		callback(null, results);
    	}
  	});
}

module.exports.getAtributoBbdd = function(callback) {
    conMySQL.appdistrack_mysql2.execute('SELECT * FROM appdistrack.atributo_bbdd', [],
  	function(err, results, fields) {
    	if(err) {
    		callback(err, null);
    	} else {
    		callback(null, results);
    	}
  	});
}

module.exports.getEstadosMolitalia = function(callback) {
    // conMySQL.appdistrack_mysql2.execute('SELECT * FROM molitalia.estados WHERE active = ? AND finaliza = ?', [1, 1],
    conMySQL.appdistrack_mysql2.execute('SELECT * FROM molitalia.estados WHERE active = ?', [1],
    function(err, results, fields) {
      if(err) {
        callback(err, null);
      } else {
        callback(null, results);
      }
    });
}

module.exports.getEstados2Molitalia = function(callback) {
    conMySQL.appdistrack_mysql2.execute('SELECT * FROM molitalia.estados_2 WHERE active = ?', [1],
    function(err, results, fields) {
      if(err) {
        callback(err, null);
      } else {
        callback(null, results);
      }
    });
}

module.exports.getPedidosPendientesMolitalia = function(callback) {
    conMySQL.appdistrack_mysql2.execute('SELECT * FROM molitalia.pedidos WHERE fechaprog=CURRENT_DATE AND estado IN (select estado from molitalia.estados where active=? and finaliza=?) order by cliente, documento', [1, 0],
    function(err, results, fields) {
      if(err) {
        callback(err, null);
      } else {
        callback(null, results);
      }
    });
}
