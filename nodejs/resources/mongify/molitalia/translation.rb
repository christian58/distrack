
table "pedidos" do
	column "indice", :integer
	column "numpedido", :string
	column "idpedido", :string
	column "localpedido", :string
	column "fechaprog", :date
	column "detalle", :string
	column "cantidad", :string
	column "volumen", :string
	column "cliente", :string
	column "telfcliente", :string
	column "dircliente", :string
	column "distcliente", :string
	column "refcliente", :string
	column "clasificacion", :string
	column "placa", :string
	column "ronda", :string
	column "ventanaini", :string
	column "ventanafin", :string
	column "orden", :integer
	column "fecentrega", :string
	column "horentrega", :string
	column "documento", :string
	column "estado", :string
	column "latitud", :string
	column "longitud", :string
	column "idplaca", :string
	column "producto", :string
	column "programado", :string
	column "observacion", :string
	column "clase", :string
	column "aux1", :string
	column "aux2", :string
	column "aux3", :string
	column "peso", :string
	column "fot_foto", :binary
	column "gui_foto", :string
	column "route_id", :integer, :references => "routes"
	column "sku", :string
	column "dni", :string
	column "f_email", :string
	column "aux4", :string
	column "fecreprog", :string
end
