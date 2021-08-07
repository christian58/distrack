table "usuarios" do
	column "idusuarios", :integer
	column "usuario", :string
	column "password", :string
	column "completo", :string
	column "max_fotos", :string
	column "organizacion", :string
	column "bbdd", :string
end

table "link_bbdd" do
	column "idlink_tabla", :integer
	column "usuario_id", :integer, :references => "usuarios"
	column "usuario", :string
	column "bbdd", :string
	column "ruta_bbdd", :string
	column "tbl_programacion", :string
	column "tbl_estados_motivos", :string
	column "tbl_estados2", :string
end

table "atributo_bbdd" do
	column "id_atributo", :integer
	column "bbdd", :string
	column "tbl_programacion", :string
	column "tbl_estado_motivos", :string
	column "tbl_estado2", :string
	column "lbl_estado2", :string
	column "cmp_vista1", :string
	column "cmp_vista2", :string
	column "ruta_bbdd", :string
	column "puerto_bbdd", :string
end