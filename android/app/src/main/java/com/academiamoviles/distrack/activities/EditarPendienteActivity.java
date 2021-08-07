package com.academiamoviles.distrack.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.models.CodigoPromotor;
import com.academiamoviles.distrack.models.EstadoCobranza;
import com.academiamoviles.distrack.models.Firma;
import com.academiamoviles.distrack.models.Foto;
import com.academiamoviles.distrack.models.RegEstado;
import com.academiamoviles.distrack.models.ResponseRegEstado;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;
import com.academiamoviles.distrack.services.AMCamera;
import com.academiamoviles.distrack.services.AMDrawManager;
import com.academiamoviles.distrack.services.AMLocationManager;
import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.academiamoviles.distrack.Constants.tarea;

public class EditarPendienteActivity extends AppCompatActivity {

    private Bundle data;
    private EditText tareaComentario;
    private RadioGroup tiposCobranza;
    private RadioButton cobranza;
    private TextView actualizarPendienteNFoto;
    private RealmResults<EstadoCobranza> estadoCobranzas;

    private ProgressDialog progress;
    private int idCobranza;
    private String nombreCobranza = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PROMOTOR", CodigoPromotor.codigo);
        CodigoPromotor.codigo = "";
        Log.d("PROMOTOR", CodigoPromotor.codigo);
        setContentView(R.layout.activity_editar_pendiente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        AMLocationManager.getInstance().setActivity(this);

        data = getIntent().getExtras();

        if (Constants.REALM == null) {
            Realm.init(this);
            Constants.REALM = Realm.getDefaultInstance();
            Constants.USUARIO = Constants.REALM.where(User.class).findFirst();
            Constants.APISEVICE = AMApiServiceGenerator.createService(AMApiService.class);
        }

        ((TextView) findViewById(R.id.tareaCliente)).setText(tarea.getCliente());
        ((TextView) findViewById(R.id.tareaDocumento)).setText(tarea.getDocumento());
        ((TextView) findViewById(R.id.tareaVolumen)).setText(tarea.getCantidad());
        ((TextView) findViewById(R.id.tareaPeso)).setText(tarea.getPeso());
        ((TextView) findViewById(R.id.tareaDircliente)).setText(tarea.getDircliente());
        ((TextView) findViewById(R.id.tareaPlanillla)).setText(tarea.getAux5());
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String tareaInicioText = "INICIADO / " + hourFormat.format(currentTime);
        ((TextView) findViewById(R.id.tareaInicio)).setText(tareaInicioText);

        TextView tareaMotivo = (TextView) findViewById(R.id.tareaMotivo);
        Button tareaEstado = (Button) findViewById(R.id.tareaEstado);
        RelativeLayout tareaEstado2 = (RelativeLayout) findViewById(R.id.tareaEstado2);
        CardView cardView4 = (CardView) findViewById(R.id.cardView4);
        ImageView botonGuardar = (ImageView) findViewById(R.id.botonGuardar);
        ImageView actualizarPendienteFoto = (ImageView) findViewById(R.id.actualizarPendienteFoto);
        tiposCobranza = (RadioGroup) findViewById(R.id.tiposCobranza);
        tareaComentario = (EditText) findViewById(R.id.tareaComentario);
        actualizarPendienteNFoto = (TextView) findViewById(R.id.actualizarPendienteNFoto);

        if (data.getBoolean("detalle", false)) {
            tareaEstado.setVisibility(View.GONE);
            tareaEstado2.setVisibility(View.VISIBLE);
            cardView4.setVisibility(View.VISIBLE);
            botonGuardar.setVisibility(View.VISIBLE);
            String msgTareaMotivo = tarea.getTareaEstado() + " / " + tarea.getTareaMotivo();
            tareaMotivo.setText(msgTareaMotivo);
            tareaComentario.setText(tarea.getComentario());
            estadoCobranzas = Constants.REALM.where(EstadoCobranza.class).findAll();
            for (int i = 0; i < estadoCobranzas.size(); i++) {
                RadioButton button = new RadioButton(this);
                button.setText(estadoCobranzas.get(i).getEstado());
                tiposCobranza.addView(button);
            }
            tiposCobranza.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    cobranza = (RadioButton) findViewById(checkedId);
                    idCobranza = tiposCobranza.indexOfChild(cobranza);
                    nombreCobranza = cobranza.getText().toString();
                    Log.d(Constants.TAG, "onCheckedChanged: " + nombreCobranza);
                    Log.d(Constants.TAG, "onCheckedChanged: " + idCobranza);
                }
            });
        } else {
            tareaEstado2.setVisibility(View.GONE);
            cardView4.setVisibility(View.GONE);
            botonGuardar.setVisibility(View.GONE);
        }

        tareaEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPendienteActivity.this, EstadoTareaActivity.class);
                data.putString("comentario", tareaComentario.getText().toString().replaceAll("\n", " "));
                intent.putExtras(data);
                EditarPendienteActivity.this.startActivity(intent);
            }
        });
        tareaEstado2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPendienteActivity.this, EstadoTareaActivity.class);
                data.putString("comentario", tareaComentario.getText().toString().replaceAll("\n", " "));
                intent.putExtras(data);
                EditarPendienteActivity.this.startActivity(intent);
            }
        });


        actualizarPendienteFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PROMOTOR_PENDIENTE", CodigoPromotor.codigo);
                CodigoPromotor.codigo = "";
                Log.d("PROMOTOR_PENDIENTE", CodigoPromotor.codigo);
                Intent intent = new Intent(EditarPendienteActivity.this, FotoPedidoActivity.class);
                intent.putExtra("idpedido", tarea.getIdpedido());
                intent.putExtra("documento", tarea.getDocumento());
                EditarPendienteActivity.this.startActivity(intent);
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTarea();
            }
        });

        ImageView actualizarFirma = (ImageView) findViewById(R.id.actualizarFirma);
        actualizarFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPendienteActivity.this, AMDrawManager.class);
                EditarPendienteActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String msgActualizarPendienteNFoto = Constants.REALM.where(Foto.class).equalTo("documentopedido", tarea.getDocumento()).findAll().size() + " Fotos";
        actualizarPendienteNFoto.setText(msgActualizarPendienteNFoto);
        String msgActualizarPendienteNFirma = Constants.REALM.where(Firma.class).equalTo("documentopedido", tarea.getDocumento()).findAll().size() + " Firma";
        ((TextView) findViewById(R.id.actualizarFirmaText)).setText(msgActualizarPendienteNFirma);
        AMLocationManager.getInstance().setActivity(this);
        if (AMLocationManager.getInstance().statusCheck()) {
            if (!AMLocationManager.getInstance().isInit()) {
                AMLocationManager.getInstance().init();
                AMLocationManager.getInstance().setInit(true);
            }
            AMLocationManager.getInstance().checkLastLocation();
        }
    }

    private void guardarTarea() {
        if (nombreCobranza.isEmpty()) {
            Toast.makeText(this, "Elija un tipo de cobranza", Toast.LENGTH_SHORT).show();
        } else if (Constants.REALM.where(Foto.class)
                .equalTo("idpedido", tarea.getIdpedido())
                .equalTo("documentopedido", tarea.getDocumento())
                .findAll().size() == 0) {
            Toast.makeText(this, "Tiene que tomar una foto como mínimo", Toast.LENGTH_SHORT).show();
        } else {
            guardarEstado(tarea);
        }
    }

    private void guardarEstado(final Tarea tarea) {
        progress = new ProgressDialog(EditarPendienteActivity.this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.setMessage("Actualizando datos...");
        progress.show();
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final RegEstado regEstado = new RegEstado();
        regEstado.setDocumento(tarea.getDocumento());
        regEstado.setUsuario(Constants.USUARIO.getUsuario());
        regEstado.setUsuario_id(Constants.USUARIO.getId());
        regEstado.setEstado(tarea.getTareaEstado());
        regEstado.setMotivo(tarea.getTareaMotivo());
        regEstado.setCobranza(estadoCobranzas.get(idCobranza).getEstado());
        regEstado.setFecha(dateFormat.format(currentTime));
        regEstado.setLatitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLatitude()));
        regEstado.setLongitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLongitude()));
        regEstado.setComentario(tareaComentario.getText().toString().replaceAll("\n", " "));
        regEstado.setCod_estado(tarea.getTareaCodMotivo());
        regEstado.setCod_motivo(tarea.getTareaCodMotivo());
        regEstado.setCod_cobranza(estadoCobranzas.get(idCobranza).getCodigo());
        List<RegEstado> regEstadoList = new ArrayList<>();
        regEstadoList.add(regEstado);
        HashMap<String, List<RegEstado>> map = new HashMap<>();
        map.put("data", regEstadoList);
        Gson gson = new Gson();
        Log.d(Constants.TAG, "guardarEstado: " + gson.toJson(map));
        Constants.APISEVICE.sendRegEstado(map).enqueue(new Callback<List<ResponseRegEstado>>() {
            @Override
            public void onResponse(Call<List<ResponseRegEstado>> call, Response<List<ResponseRegEstado>> response) {
                Log.d(Constants.TAG, "onResponseGuardarEstado:" + response.body().get(0).getSuccess());
                AMCamera.subirDatosTarea(true, tarea,
                        AMLocationManager.getInstance().getLastLocation().getLatitude(),
                        AMLocationManager.getInstance().getLastLocation().getLongitude());
                Intent intent = new Intent(EditarPendienteActivity.this, TareasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                progress.dismiss();
                finish();
                EditarPendienteActivity.this.startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<ResponseRegEstado>> call, Throwable t) {
                Log.d(Constants.TAG, "onFailure: " + t.toString());
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(regEstado);
                        tarea.setSincronizar(true);
                    }
                });
                Intent intent = new Intent(EditarPendienteActivity.this, TareasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                progress.dismiss();
                finish();
                EditarPendienteActivity.this.startActivity(intent);
            }
        });
    }
}
