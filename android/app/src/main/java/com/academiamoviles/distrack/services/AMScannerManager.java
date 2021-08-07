package com.academiamoviles.distrack.services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.activities.ActualizarPendienteActivity;
import com.academiamoviles.distrack.activities.DetalleTareaActivity;
import com.academiamoviles.distrack.activities.EditarPendienteActivity;
import com.academiamoviles.distrack.activities.TareasActivity;
import com.academiamoviles.distrack.models.RegEstado;
import com.academiamoviles.distrack.models.ResponseQR;
import com.academiamoviles.distrack.models.ResponseRegEstado;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usuario on 9/23/2017.
 */

public class AMScannerManager extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constants.REALM == null) {
            Realm.init(this);
            Constants.REALM = Realm.getDefaultInstance();
            Constants.USUARIO = Constants.REALM.where(User.class).findFirst();
            Constants.APISEVICE = AMApiServiceGenerator.createService(AMApiService.class);
        }

        IntentIntegrator integrator = new IntentIntegrator(AMScannerManager.this);
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Enfoque el código");
        //integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

//        setContentView(R.layout.amscannermanager);
//        etInfo=(EditText)findViewById(R.id.editText);
//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IntentIntegrator integrator = new IntentIntegrator(AMScannerManager.this);
//                //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setPrompt("Buscando codigo");
//                //integrator.setCameraId(0);
//                integrator.setBeepEnabled(true);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getApplicationContext(), "No se logro escanear", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AMScannerManager.this, TareasActivity.class));
            } else {
                Tarea tarea = Constants.REALM.where(Tarea.class).equalTo("documento", result.getContents()).findFirst();
                //Toast.makeText(getApplicationContext(), "Código obtenido: " + result.getContents(), Toast.LENGTH_SHORT).show();
                if (tarea != null) {
                    if (tarea.getIniciado2()) {
                        Intent intent = new Intent(this, EditarPendienteActivity.class);
                        Constants.tarea = tarea;
                        intent.putExtra("detalle", true);
                        startActivity(intent);
                        finish();
                    } else if (tarea.getIniciado1()) {
                        Intent intent = new Intent(this, EditarPendienteActivity.class);
                        intent.putExtra("detalle", false);
                        Constants.tarea = tarea;
                        startActivity(intent);
                        finish();
                    } else if (tarea.getLlegada()) {
                        Intent intent = new Intent(this, ActualizarPendienteActivity.class);
                        intent.putExtra("detalle", false);
                        Constants.tarea = tarea;
                        startActivity(intent);
                        finish();
                    } else {
                        alertaTarea(this, tarea).show();
                    }
                } else {
                    AMApiServiceGenerator.createService(AMApiService.class).getQR(result.getContents()).enqueue(new Callback<ResponseQR>() {
                        @Override
                        public void onResponse(Call<ResponseQR> call, Response<ResponseQR> response) {
                            Log.d(Constants.TAG, "onResponse3: " + response.body().getSuccess());
                            if (response.body().getSuccess()) {
                                final Tarea tarea1 = response.body().getPedido();
                                Constants.REALM.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealm(tarea1);
                                    }
                                });
                                alertaTareaNueva(AMScannerManager.this, tarea1).show();
                            } else {
                                final Tarea tareaNueva = new Tarea();
                                tareaNueva.setDocumento(result.getContents());
                                Constants.REALM.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealm(tareaNueva);
                                    }
                                });
                                alertaTareaNueva(AMScannerManager.this, tareaNueva).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseQR> call, Throwable t) {
                            Log.e(Constants.TAG, "onFailure: ", t);
                            final Tarea tareaNueva = new Tarea();
                            tareaNueva.setDocumento(result.getContents());
                            Constants.REALM.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(tareaNueva);
                                }
                            });
                            alertaTareaNueva(AMScannerManager.this, tareaNueva).show();
                        }
                    });
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private AlertDialog alertaTarea(final Context context, final Tarea tarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constants.MENSAJE_TAREACONFIRMACION);
        builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, ActualizarPendienteActivity.class);
                Constants.tarea = tarea;
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Tarea.class)
                                .equalTo("idpedido", tarea.getIdpedido())
                                .equalTo("documento", tarea.getDocumento())
                                .findFirst()
                                .setLlegada(true);
                    }
                });
                guardarEstado(tarea.getDocumento());
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, DetalleTareaActivity.class);
                Constants.tarea = tarea;
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
        return builder.create();
    }

    private AlertDialog alertaTareaNueva(final Context context, final Tarea tarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constants.MENSAJE_TAREACONFIRMACION);
        builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, ActualizarPendienteActivity.class);
                Constants.tarea = tarea;
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Tarea.class)
                                .equalTo("idpedido", tarea.getIdpedido())
                                .equalTo("documento", tarea.getDocumento())
                                .findFirst()
                                .setLlegada(true);
                    }
                });
                guardarEstado(tarea.getDocumento());
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, TareasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Tarea.class)
                                .equalTo("idpedido", tarea.getIdpedido())
                                .equalTo("documento", tarea.getDocumento())
                                .findFirst()
                                .deleteFromRealm();
                    }
                });
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        });
        return builder.create();
    }

    private void guardarEstado(String documento) {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final RegEstado regEstado = new RegEstado();
        regEstado.setDocumento(documento);
        regEstado.setUsuario(Constants.USUARIO.getUsuario());
        regEstado.setUsuario_id(Constants.USUARIO.getId());
        regEstado.setEstado("Llegada");
        regEstado.setMotivo("Llegada");
        regEstado.setCobranza("");
        regEstado.setFecha(dateFormat.format(currentTime));
        regEstado.setLatitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLatitude()));
        regEstado.setLongitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLongitude()));
        regEstado.setComentario("");
        regEstado.setCod_estado("EST0000");
        regEstado.setCod_motivo("EST0000");
        regEstado.setCod_cobranza("");
        List<RegEstado> regEstadoList = new ArrayList<>();
        regEstadoList.add(regEstado);
        HashMap<String, List<RegEstado>> map = new HashMap<>();
        map.put("data", regEstadoList);
        Constants.APISEVICE.sendRegEstado(map).enqueue(new Callback<List<ResponseRegEstado>>() {
            @Override
            public void onResponse(Call<List<ResponseRegEstado>> call, Response<List<ResponseRegEstado>> response) {
                Log.d(Constants.TAG, "onResponseGuardarEstado: " + response.body().get(0).getSuccess());
            }

            @Override
            public void onFailure(Call<List<ResponseRegEstado>> call, Throwable t) {
                Log.d(Constants.TAG, "onFailure: " + t.toString());
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(regEstado);
                    }
                });
            }
        });
    }
}
