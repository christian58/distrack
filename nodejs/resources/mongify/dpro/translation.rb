table "campos" do
	column "indice", :integer
	column "campo", :string
	column "etiqueta", :string
end

table "est_beetrack" do
	column "indice", :integer
	column "est_beetrack", :string
	column "est_paraiso", :string
	column "isubest_beetrack", :integer
	column "iest_beetrack", :integer
end

table "estadopedidos" do
	column "indice", :integer
	column "idpedido", :string
	column "iestado", :integer
	column "estado", :string
	column "subestado", :string
	column "estadoanterior", :string
	column "fechaedicion", :datetime
	column "gui_foto", :string
	column "idplaca", :string
	column "lon", :float
	column "lat", :float
	column "isubestado", :integer
	column "fechaestado", :datetime
end

table "estadopedidosh" do
	column "correlativo", :integer
	column "fechaop", :datetime
	column "indice", :integer
	column "idpedido", :string
	column "iestado", :integer
	column "estado", :string
	column "subestado", :string
	column "estadoanterior", :string
	column "fechaedicion", :datetime
	column "gui_foto", :string
	column "idplaca", :string
	column "lon", :float
	column "lat", :float
	column "isubestado", :integer
	column "fechaestado", :datetime
end

table "fotopedidos" do
	column "indice", :integer
	column "idpedido", :string
	column "iestado", :integer
	column "estado", :string
	column "subestado", :string
	column "estadoanterior", :string
	column "fechaedicion", :datetime
	column "gui_foto", :string
	column "idplaca", :string
	column "lon", :float
	column "lat", :float
	column "isubestado", :integer
end

table "mudanza" do
	column "indice", :integer
	column "codebar", :string
	column "cliente", :string
end

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

table "pedidosbeetrack" do
	column "indice", :integer
	column "fechaprog", :date
	column "placa", :string
	column "ronda", :string
	column "estado", :string
	column "producto", :string
	column "idpedido", :string
	column "cliente", :string
	column "dircliente", :string
	column "detalle", :string
	column "cantidad", :string
	column "numpedido", :string
	column "route_id", :integer, :references => "routes"
	column "fechainsercion", :datetime
	column "idplaca", :string
	column "gui_foto", :string
	column "dni", :string
	column "telfcliente", :string
	column "fechaestado", :datetime
end

table "pedidosbeetrackh" do
	column "correlativo", :integer
	column "fechaop", :datetime
	column "indice", :integer
	column "fechaprog", :date
	column "placa", :string
	column "ronda", :string
	column "estado", :string
	column "producto", :string
	column "idpedido", :string
	column "cliente", :string
	column "dircliente", :string
	column "detalle", :string
	column "cantidad", :string
	column "numpedido", :string
	column "route_id", :integer, :references => "routes"
	column "fechainsercion", :datetime
	column "idplaca", :string
	column "gui_foto", :string
	column "dni", :string
	column "telfcliente", :string
	column "fechaestado", :datetime
end

table "pedidosbeetrackproceso" do
	column "indice", :integer
	column "fechaprog", :date
	column "placa", :string
	column "ronda", :string
	column "producto", :string
	column "idpedido", :string
	column "cliente", :string
	column "dircliente", :string
	column "detalle", :string
	column "cantidad", :string
	column "numpedido", :string
	column "operacion", :string
	column "route_id", :integer, :references => "routes"
	column "procesado", :string
	column "fechainsercion", :datetime
	column "dni", :string
	column "telfcliente", :string
	column "idplaca", :string
end

table "pedidosbeetrackprocesoh" do
	column "correlativo", :integer
	column "fechaop", :datetime
	column "indice", :integer
	column "fechaprog", :date
	column "placa", :string
	column "ronda", :string
	column "producto", :string
	column "idpedido", :string
	column "cliente", :string
	column "dircliente", :string
	column "detalle", :string
	column "cantidad", :string
	column "numpedido", :string
	column "operacion", :string
	column "route_id", :integer, :references => "routes"
	column "procesado", :string
	column "fechainsercion", :datetime
	column "dni", :string
	column "telfcliente", :string
	column "idplaca", :string
end

table "placas_vigentes" do
	column "placa", :string
	column "fechaprog", :date
end

table "posicionpedidosbeetrack" do
	column "numcorrelativo", :integer
	column "idplaca", :string
	column "route_id", :integer, :references => "routes"
	column "fechaprog", :date
	column "ronda", :string
	column "lat", :float
	column "lon", :float
	column "fechainsercion", :datetime
	column "envio", :string
end

table "posicionpedidosbeetrackh" do
	column "correlativo", :integer
	column "fechaop", :datetime
	column "numcorrelativo", :integer
	column "idplaca", :string
	column "route_id", :integer, :references => "routes"
	column "fechaprog", :date
	column "ronda", :string
	column "lat", :float
	column "lon", :float
	column "fechainsercion", :datetime
	column "envio", :string
end

table "puntero" do
	column "tablaorigen", :string
	column "ultimoregistro", :string
end

table "system_users" do
	column "ID", :key, :as => :integer
	column "client_key", :string
	column "FullName", :string
	column "Address", :string
	column "ContactName1", :string
	column "ContactName2", :string
	column "ContactPhone1", :string
	column "ContactPhone2", :string
	column "Notes", :string
	column "RemoteUserName", :string
	column "RemotePassword", :string
	column "TCPRemoteActive", :boolean
	column "TCPRemoteCharged", :boolean
	column "TCPRemoteCost", :integer
	column "TCPRemoteCredit", :integer
	column "TCPRemoteControl", :boolean
	column "SMSRemoteNumber", :string
	column "SMSRemoteActive", :boolean
	column "SMSRemoteCharged", :boolean
	column "SMSRemoteCredit", :integer
	column "SMSRemoteCost", :integer
	column "WebRemoteActive", :boolean
	column "WebRemoteCharged", :boolean
	column "WebRemoteCost", :integer
	column "WebRemoteCredit", :integer
	column "UserLanguage", :string
	column "StopDate", :datetime
	column "InDate", :boolean
	column "Unlimited", :boolean
	column "Staff", :boolean
	column "DataAdded", :datetime
	column "Hardware", :string
	column "Hardware2", :string
	column "Hardware3", :string
	column "Administrator", :boolean
	column "CompanyName", :string
	column "EMailAddress", :string
	column "GroupModeLogin", :boolean
	column "AllowLocate", :boolean
	column "AllowZones", :boolean
	column "AllowReports", :boolean
	column "AllowRoutes", :boolean
	column "AllowDetailChange", :boolean
	column "AllowCommandSending", :boolean
	column "CompanyCode", :string
	column "User_Password", :string
	column "User_Group1", :string
	column "User_Group2", :string
	column "User_Group3", :string
	column "WebGroup", :string
	column "Mobile", :string
	column "AllowWebAutoUpdate", :boolean
	column "AllowGroupReports", :boolean
	column "AllowUpgrades", :boolean
	column "UpgradeURL", :string
	column "AllowWireTap", :boolean
	column "AllowContactDetails", :boolean
	column "MaxHistoryRecords", :integer
	column "ZonesReadOnly", :boolean
	column "AllowResponses", :boolean
	column "ResponsesReadOnly", :boolean
	column "AllowGridRemoval", :boolean
	column "AllowWebEmailSetting", :boolean
	column "AllowIconColourControl", :boolean
	column "AllowCustomPointImport", :boolean
	column "AllowAdminReports", :boolean
	column "EngineerOrInstaller", :boolean
	column "AllowContactDetailsChanging", :boolean
	column "AllowRegistrationChanging", :boolean
	column "AllowAlertEmailChanging", :boolean
	column "AllowRFIDEditing", :boolean
	column "AllowMapSearch", :boolean
	column "AllowVehicleTypeChanging", :boolean
	column "AllowIconColourChanging", :boolean
	column "AllowSMS_GSM_Mode", :boolean
	column "AllowMileageEntry", :boolean
	column "AllowHoursEntry", :boolean
	column "WebPassword", :string
	column "client_active", :boolean
	column "allow_barcode_reports", :boolean
	column "allow_barcode_data_entry", :boolean
	column "allow_analogue_reports", :boolean
	column "system_settings_password", :string
	column "allow_status_updating", :boolean
	column "allow_MDT_reading", :boolean
	column "allow_MDT_Sending", :boolean
	column "allow_tab_file_import", :boolean
	column "report_emails", :string
	column "allow_vehicle_maintenance", :boolean
	column "allow_point_emailing", :boolean
	column "allow_sub_login_control", :boolean
	column "allow_gmaps", :boolean
	column "allow_raw_report", :boolean
	column "allow_analog_fuel_report_1", :boolean
	column "allow_analog_fuel_report_2", :boolean
	column "allow_analog_fuel_report_1_graphical", :boolean
	column "allow_analog_fuel_report_2_graphical", :boolean
	column "allow_sms_email_user_points", :boolean
	column "allow_rfid", :boolean
	column "allow_distance_reminders", :boolean
	column "allow_status_change", :boolean
	column "allow_A1_fuel_reports", :boolean
	column "allow_taxi_door_reports", :boolean
end

table "tbl_foto" do
	column "fot_id", :integer, :references => "fots"
	column "gui_nroguia", :string
	column "gui_fecha", :string
	column "fot_foto", :binary
end

table "tbl_nroguia" do
	column "GUI_ID", :integer, :references => "guis"
	column "GUI_NROGUIA", :string
	column "GUI_FECHA", :string
	column "latitud", :string
	column "longitud", :string
	column "celda", :string
end

table "unidades" do
	column "id", :key, :as => :string
	column "placa", :string
	column "imgplaca", :string
	column "conductor", :string
	column "responsable", :string
	column "apoyo1", :string
	column "apoyo2", :string
	column "apoyo3", :string
	column "apoyo4", :string
	column "imgconductor", :string
	column "imgresponsable", :string
	column "imgapoyo1", :string
	column "imgapoyo2", :string
	column "imgapoyo3", :string
	column "imgapoyo4", :string
	column "transportista", :string
	column "grupoplaca", :string
end

