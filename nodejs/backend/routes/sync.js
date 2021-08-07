var express = require('express');
var router = express.Router();
var async = require('async');
var syncController = require('../controllers/syncController');
var usuarioController = require('../controllers/usuarioController');
var linkbbddController = require('../controllers/linkbbddController');
var atributoController = require('../controllers/atributoController');
var estadoController = require('../controllers/estadoController');
var estado2Controller = require('../controllers/estado2Controller');
var pedidosController = require('../controllers/pedidoController');

router.get('/', function(req, res, next) {
    res.send('routes sync...');
});

router.get('/mysql-to-mongo', function(req, res) {
	syncController.getUsuarios(function(error, usuarios) {
        if(error) {
        	console.log(error);
        } else {
        	
        	var response = [];

        	usuarioController.deleteUsuario(function(successUsuario) {
        		if(successUsuario) {

	        		async.each(usuarios, function(item, done) {
			   		    var params = {
					        idusuarios: item.idusuarios,
					        usuario: item.usuario,
					        password: item.password,
					        completo: item.completo,
					        max_fotos: item.max_fotos,
					        organizacion: item.organizacion,
					        bbdd: item.bbdd,
					        sesion: item.sesion
					    }

					    usuarioController.addUsuario(params, function(data) {
		                    if(data) {
		                    	done();
		                    } else {
		                    	done(data);
		                    }
		                });
		            }, function(err) {
		                if(err) {
		                	console.log('Error al cargar usuarios');
		                	response.push({label: 'Usuarios', success: false});
		                	res.json(response);
		                } else {
		                	console.log('Carga exitosa de usuarios.');
		                	response.push({label: 'Usuarios', success: true});

    		               	// [START LINKBBDD]
		                	syncController.getLinkBbdd(function(errorLinkbbdd, linkbbdd) {
		                		if(errorLinkbbdd) {
		                			console.log(errorLinkbbdd);
		                		} else {
		                			linkbbddController.deleteLinkbbdd(function(successLinkbbdd) {
		                				if(successLinkbbdd) {

		                					async.each(linkbbdd, function(itemlinkbbdd, doneLinkbbdd) {
		                					
									   		    var paramsLinkbbdd = {
											        idlink_tabla: itemlinkbbdd.idlink_tabla,
											        usuario: itemlinkbbdd.usuario,
											        bbdd: itemlinkbbdd.bbdd,
											        ruta_bbdd: itemlinkbbdd.ruta_bbdd,
											        tbl_programacion: itemlinkbbdd.tbl_programacion,
											        tbl_estados_motivos: itemlinkbbdd.tbl_estados_motivos,
											        tbl_estados2: itemlinkbbdd.tbl_estados2
											    }

		                						linkbbddController.addLinkbbdd(paramsLinkbbdd, function(dataLinkbbdd) {
								                    if(dataLinkbbdd) {
								                    	doneLinkbbdd();
								                    } else {
								                    	doneLinkbbdd(dataLinkbbdd);
								                    }
								                });

		                					}, function(err) {
								                if(err) {
								                	console.log('Error al cargar linkbbdd.');
								                	response.push({label: 'Linkbbdd', success: false});
								                	res.json(response);
								                } else {
								                	console.log('Carga exitosa de linkbbdd.');
								                	response.push({label: 'Linkbbdd', success: true});

								                	// [START ATRIBUTOBBDD]
								                	syncController.getAtributoBbdd(function(errorAtributobbdd, atributobbdd) {
								                		atributoController.deleteAtributoBbdd(function(successAtributobbdd) {
								                			if(successAtributobbdd) {
								                				async.each(atributobbdd, function(itemAtributobbdd, doneAtributobbdd) {
								                					
								                					var paramsAtributobbdd = {
																        id_atributo: itemAtributobbdd.id_atributo,
																        bbdd: itemAtributobbdd.bbdd,
																        tbl_programacion: itemAtributobbdd.tbl_programacion,
																        tbl_estado_motivos: itemAtributobbdd.tbl_estado_motivos,
																        tbl_estado2: itemAtributobbdd.tbl_estado2,
																        lbl_estado2: itemAtributobbdd.lbl_estado2,
																        cmp_vista1: itemAtributobbdd.cmp_vista1,
																        cmp_vista2: itemAtributobbdd.cmp_vista2,
																        ruta_bbdd: itemAtributobbdd.ruta_bbdd,
																        puerto_bbdd: itemAtributobbdd.puerto_bbdd								                						
								                					}

								                					atributoController.addAtributoBbdd(paramsAtributobbdd, function(dataAtributobbdd) {
								                						if(dataAtributobbdd) {
								                							
								                							doneAtributobbdd();
								                						} else {
								                							
								                							dataAtributobbdd(dataAtributobbdd);
								                						}
								                					});

								                				}, function(err) {
								                					if(err) {
								                						console.log('Error al cargar atributodd.');
								                						response.push({label: 'Atributobbdd', success: false});
								                						res.json(response);
								                					} else {
								                						console.log('Carga exitosa de atributodd.');
								                						response.push({label: 'Atributobbdd', success: true});
								                						
								                						// [START ESTADO MOLITALIA]
								                						syncController.getEstadosMolitalia(function(errorEstadoMolitalia, estadoMolitalia) {
								                							estadoController.deleteEstados(function(successEstadoMolitalia) {
								                								if(successEstadoMolitalia) {
																					async.each(estadoMolitalia, function(itemEstadoMolitalia, doneEstadoMolitalia) {
																						
																						var paramsEstadoMolitalia = {
								                											codigo: itemEstadoMolitalia.codigo,
																					        motivos: itemEstadoMolitalia.motivos,
																					        estado: itemEstadoMolitalia.estado,
																					        active: itemEstadoMolitalia.active,
																					        finaliza: itemEstadoMolitalia.finaliza
								                										}

								                										estadoController.addEstados(paramsEstadoMolitalia, function(dataEstadoMolitalia) {
								                											if(dataEstadoMolitalia) {
								                												
								                												doneEstadoMolitalia();
								                											} else {
								                												
								                												doneEstadoMolitalia(dataEstadoMolitalia);
								                											}
								                										});

								                									}, function(err) {
								                										if(err) {
								                											console.log('Error al cargar estado molitalia');
								                											response.push({label: 'Estado molitalia', success: false});
													                						res.json(response);
								                										} else {
								                											console.log('Carga exitosa de estado molitalia');
								                											response.push({label: 'Estado molitalia', success: true});

								                											// [START ESTADO 2]

								                											syncController.getEstados2Molitalia(function(errorEstado2Molitalia, estado2Molitalia) {
								                												estado2Controller.deleteEstados2(function(successEstado2Molitalia) {
								                													if(successEstado2Molitalia) {
								                														async.each(estado2Molitalia, function(itemEstado2Molitalia, doneEstado2Molitalia) {
								                														
													                										var paramsEstado2Molitalia = {
																										        estado: itemEstado2Molitalia.estado,
																										        descripcion: itemEstado2Molitalia.descripcion,
																										        active: itemEstado2Molitalia.active,
																										        codigo: itemEstado2Molitalia.codigo
													                										}

													                										estado2Controller.addEstados2(paramsEstado2Molitalia, function(dataEstado2Molitalia) {
													                											if(dataEstado2Molitalia) {
													                												doneEstado2Molitalia();
													                											} else {
													                												doneEstado2Molitalia(dataEstado2Molitalia);
													                											}
													                										});

								                														}, function(err) {
																											if(err) {
													                											console.log('Error al cargar estado2 molitalia');
													                											response.push({label: 'Estado2 molitalia', success: false});
																		                						res.json(response);
																											} else {
													                											console.log('Carga exitosa de estado2 molitalia');
													                											response.push({label: 'Estado2 molitalia', success: true});

																												getPedidosPendientes(res, response);
																											}
																										});
								                													} else {
													                									console.log('not delete estado2 molitalia');
													                									res.json(response);
								                													}
								                												});
								                											});

								                											// [END ESTADO 2]
								                										}
								                									});
								                								} else {
								                									console.log('not delete estado molitalia');
								                									res.json(response);
								                								}
								                							});
								                						});
								                						// [END ESTADO MOLITALIA]
								                					}
								                				});
								                			} else {
								                				console.log('not delete atributobbdd');
								                				res.json(response);
								                			}
								                		});
								                	});
								                	// [END ATRIBUTOBBDD]

								                }
		                					});

		                				} else {
        									console.log('not delete linkbbdd');
        									res.json(response);
		                				}
		                			});
		                		}
		                	});
		                	// [END LINKBBDD]

		                }
		            });

        		} else {
        			console.log('not delete usuario');
        			res.json(response);
        		}
        	});

        }
    });
});

function getPedidosPendientes(res, response) {
	// [START ESTADO 2]

	syncController.getPedidosPendientesMolitalia(function(errorPedidosPendientesMolitalia, pedidosPendientesMolitalia) {
		if(errorPedidosPendientesMolitalia) {
        	console.log(errorPedidosPendientesMolitalia);
			response.push({label: 'Pedidos molitalia', success: false});
			res.json(response);
		} else {

        	pedidosController.deletePedido(function(successPedienteMolitalia) {
        		if(successPedienteMolitalia) {

        			async.each(pedidosPendientesMolitalia, function(itemPedidosPendientesMolitalia, donePedidosPendientesMolitalia) {

						var paramsPedidosPendientes = {
					        indice: itemPedidosPendientesMolitalia.indice,
					        numpedido: itemPedidosPendientesMolitalia.numpedido,
					        idpedido: itemPedidosPendientesMolitalia.idpedido,
					        localpedido: itemPedidosPendientesMolitalia.localpedido,
					        fechaprog: itemPedidosPendientesMolitalia.fechaprog,
					        detalle: itemPedidosPendientesMolitalia.detalle,
					        cantidad: itemPedidosPendientesMolitalia.cantidad,
					        volumen: itemPedidosPendientesMolitalia.volumen,
					        cliente: itemPedidosPendientesMolitalia.cliente,
					        telfcliente: itemPedidosPendientesMolitalia.telfcliente,
					        dircliente: itemPedidosPendientesMolitalia.dircliente,
					        distcliente: itemPedidosPendientesMolitalia.distcliente,
					        refcliente: itemPedidosPendientesMolitalia.refcliente,
					        codproducto: itemPedidosPendientesMolitalia.codproducto,
					        placa: itemPedidosPendientesMolitalia.placa,
					        ronda: itemPedidosPendientesMolitalia.ronda,
					        ventanaini: itemPedidosPendientesMolitalia.ventanaini,
					        ventanafin: itemPedidosPendientesMolitalia.ventanafin,
					        orden: itemPedidosPendientesMolitalia.orden,
					        fecentrega: itemPedidosPendientesMolitalia.fecentrega,
					        horentrega: itemPedidosPendientesMolitalia.horentrega,
					        documento: itemPedidosPendientesMolitalia.documento,
					        estado: itemPedidosPendientesMolitalia.estado,
					        latitud: itemPedidosPendientesMolitalia.latitud,
					        longitud: itemPedidosPendientesMolitalia.longitud,
					        idplaca: itemPedidosPendientesMolitalia.idplaca,
					        producto: itemPedidosPendientesMolitalia.producto,
					        sede: itemPedidosPendientesMolitalia.sede,
					        peso: itemPedidosPendientesMolitalia.peso,
					        prestaciones: itemPedidosPendientesMolitalia.prestaciones,
					        clase: itemPedidosPendientesMolitalia.clase,
					        nombre: itemPedidosPendientesMolitalia.nombre,
					        aux1: itemPedidosPendientesMolitalia.aux1,
					        aux2: itemPedidosPendientesMolitalia.aux2,
					        aux3: itemPedidosPendientesMolitalia.aux3,
					        aux4: itemPedidosPendientesMolitalia.aux4,
					        aux5: itemPedidosPendientesMolitalia.aux5,
					        aux6: itemPedidosPendientesMolitalia.aux6,
					        aux7: itemPedidosPendientesMolitalia.aux7,
					        aux8: itemPedidosPendientesMolitalia.aux8,
					        aux9: itemPedidosPendientesMolitalia.aux9,
					        aux10: itemPedidosPendientesMolitalia.aux10,
					        observacion: itemPedidosPendientesMolitalia.observacion,
					        aux11: itemPedidosPendientesMolitalia.aux11,
					        aux12: itemPedidosPendientesMolitalia.aux12,
					        fupdate: itemPedidosPendientesMolitalia.fupdate,
					        aux13: itemPedidosPendientesMolitalia.aux13,
					        aux14: itemPedidosPendientesMolitalia.aux14,
					        fot_foto: itemPedidosPendientesMolitalia.fot_foto,
					        u_vendedor: itemPedidosPendientesMolitalia.u_vendedor,
					        u_supervisor: itemPedidosPendientesMolitalia.u_supervisor,
					        u_transportista: itemPedidosPendientesMolitalia.u_transportista,
					        f_email: itemPedidosPendientesMolitalia.f_email,
					        hora_llegada: itemPedidosPendientesMolitalia.hora_llegada,
					        cobranza: itemPedidosPendientesMolitalia.cobranza,
					        fh_update: itemPedidosPendientesMolitalia.fh_update,
					        fg_finaliza: itemPedidosPendientesMolitalia.fg_finaliza
						}

						pedidosController.existPedido(paramsPedidosPendientes, function(existPedidoPendiente) {
							if(!existPedidoPendiente) {
								pedidosController.addPedido(paramsPedidosPendientes, function(dataPedidosPendientesMolitalia) {
									if(dataPedidosPendientesMolitalia) {
										donePedidosPendientesMolitalia();
									} else {
										donePedidosPendientesMolitalia(dataPedidosPendientesMolitalia);
									}
								});
							} else {
								pedidosController.updatePedido(paramsPedidosPendientes, function(dataPedidosPendientesMolitalia) {
									donePedidosPendientesMolitalia();
								});
							}
						});

					}, function(err) {
						if(err) {
							console.log('Error al cargar pedidos molitalia');
							response.push({label: 'Pedidos molitalia', success: false});
							res.json(response);
						} else {
							console.log('Carga exitosa de pedidos molitalia');
							response.push({label: 'Pedidos molitalia', success: true});
							res.json(response);
						}
					});
				} else {
					console.log('Error al cargar pedidos molitalia');
					response.push({label: 'Pedidos molitalia', success: false});
					res.json(response);
				}
			});
		}
	});

	// [END ESTADO 2]
}

module.exports = router;