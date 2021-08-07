package com.academiamoviles.distrack.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.models.CodigoPromotor;
import com.academiamoviles.distrack.models.Estado;
import com.academiamoviles.distrack.models.Firma;
import com.academiamoviles.distrack.models.Foto;
import com.academiamoviles.distrack.models.Motivo;
import com.academiamoviles.distrack.models.RegEstado;
import com.academiamoviles.distrack.models.ResponseEntregaEspecial;
import com.academiamoviles.distrack.models.ResponseRegEstado;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;
import com.academiamoviles.distrack.services.AMDrawManager;
import com.academiamoviles.distrack.services.AMLocationManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

import static com.academiamoviles.distrack.Constants.tarea;
import static com.academiamoviles.distrack.Constants.USUARIO;


public class ActualizarPendienteActivity extends AppCompatActivity {

    private int inputSelection;
    private TextView actualizarPendienteNFoto;
    private Estado estado;
    AlertDialog.Builder builder;
    AlertDialog.Builder builderHecho;
    AlertDialog.Builder builderTakePhoto;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_pendiente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

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
        Button iniciarButton = (Button) findViewById(R.id.iniciarButton);
        Button retornarButton = (Button) findViewById(R.id.retornarButton);
        ImageView actualizarPendienteFoto = (ImageView) findViewById(R.id.actualizarPendienteFoto);
        ImageView actualizarFirma = (ImageView) findViewById(R.id.actualizarFirma);
        actualizarPendienteNFoto = (TextView) findViewById(R.id.actualizarPendienteNFoto);

        Button iniciarFind = (Button) findViewById(R.id.iniciarButtonFind);
        final EditText textQuery = (EditText) findViewById(R.id.editTextQuery);
        builder = new AlertDialog.Builder(this);
        builderHecho = new AlertDialog.Builder(this);
        builderTakePhoto = new AlertDialog.Builder(this);

        dialogBuilder = new AlertDialog.Builder(this);
        alertDialog = dialogBuilder.create();

        iniciarFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BUSCANDO", "onClick: ");
                if(textQuery.getText().toString() != null){
                    consultaEstadoPromotor(textQuery.getText().toString());

//                    RequestQueue queue = Volley.newRequestQueue(this);
//                    String url ="http://www.google.com";
//                    Toast.makeText(getApplicationContext(), textQuery.getText().toString(), Toast.LENGTH_LONG).show();//display the text that you entered in edit text

                }
            }
        });


        for (int i = 0; i < Constants.REALM.where(RegEstado.class).findAll().size(); i++) {
            Log.d(Constants.TAG, "regEstado: " + Constants.REALM.where(RegEstado.class).findAll().get(i).toString());
        }
        retornarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornarTarea();
            }
        });

        iniciarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodigoPromotor.codigo = "";
                Log.d("PROMOTOR_ONCLICK", CodigoPromotor.codigo);
                Intent intent = new Intent(ActualizarPendienteActivity.this, EditarPendienteActivity.class);
                intent.putExtra("detalle", false);
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        /*realm.where(Tarea.class)
                                .equalTo("idpedido", tarea.getIdpedido())
                                .equalTo("documento", tarea.getDocumento())
                                .findFirst().setIniciado1(true);*/
                        tarea.setIniciado1(true);
                    }
                });
                guardarEstadoInicio();
                ActualizarPendienteActivity.this.startActivity(intent);
                finish();
            }
        });

        actualizarFirma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActualizarPendienteActivity.this, AMDrawManager.class);
                ActualizarPendienteActivity.this.startActivity(intent);
            }
        });

        actualizarPendienteFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActualizarPendienteActivity.this, FotoPedidoActivity.class);
                intent.putExtra("idpedido", tarea.getIdpedido());
                intent.putExtra("documento", tarea.getDocumento());
                ActualizarPendienteActivity.this.startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        String msgActualizarPendienteNFoto = Constants.REALM.where(Foto.class).equalTo("documentopedido", tarea.getDocumento()).findAll().size() + " Fotos";
        String msgActualizarPendienteNFirma = Constants.REALM.where(Firma.class).equalTo("documentopedido", tarea.getDocumento()).findAll().size() + " Firma";
        ((TextView) findViewById(R.id.actualizarFirmaText)).setText(msgActualizarPendienteNFirma);
        actualizarPendienteNFoto.setText(msgActualizarPendienteNFoto);
        AMLocationManager.getInstance().setActivity(this);
        if (AMLocationManager.getInstance().statusCheck()) {
            if (!AMLocationManager.getInstance().isInit()) {
                AMLocationManager.getInstance().init();
                AMLocationManager.getInstance().setInit(true);
            }
            AMLocationManager.getInstance().checkLastLocation();
        }
    }

    Tarea copyTarea(String codigo){
        Tarea newTarea = new Tarea();
        newTarea.setAux5("0");
        newTarea.setCantidad(tarea.getCantidad());
        newTarea.setCliente(tarea.getCliente());
        newTarea.setComentario(tarea.getComentario());
        newTarea.setDircliente(tarea.getDircliente());
        newTarea.setDocumento(tarea.getDocumento());
        newTarea.setEncontrado(tarea.getEncontrado());
        newTarea.setIdpedido(tarea.getIdpedido());
        newTarea.setIniciado1(tarea.getIniciado1());
        newTarea.setIniciado2(tarea.getIniciado2());
        newTarea.setLlegada(tarea.getLlegada());
        newTarea.setPeso(tarea.getPeso());
        newTarea.setSincronizar(tarea.getSincronizar());
        newTarea.setTareaCodEstado(tarea.getTareaCodEstado());
        newTarea.setTareaCodMotivo(tarea.getTareaCodMotivo());
        newTarea.setTareaEstado(tarea.getTareaEstado());
        newTarea.setTareaMotivo(tarea.getTareaMotivo());

        return newTarea;
    }

    private void consultaEstadoPromotor(final String codigo){
        RequestQueue queue = Volley.newRequestQueue(this);

        String subString = "http://tracklogservice.com:3010/ws/entregaEspecial/consultaEstado?docPersonal=";
        subString = subString + codigo;
        String url = subString + "&idProducto=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(response);

                Log.d("REsponse", response);
                Log.d("REsponsee", String.valueOf(json.get("data")));
                JsonObject dataObject = json.getAsJsonObject("data");
                Log.d("ESTADO", String.valueOf(dataObject.get("estado")));
                String estadoRes = String.valueOf(dataObject.get("estado"));
                if(estadoRes.equals("0")){
                    String noEntregado = " Actualizado";
                    String materialesEntregado = "Se acaba de realizar la entrega de: ";
                    builder.setTitle(codigo + " - " + dataObject.get("mensaje") );
                    builder.setMessage("¿Realizar ahora la entrega?");
                    builder.setNegativeButton("Cancelar", null);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String msgActualizarPendienteNFotoPromotor = Constants.REALM.where(Foto.class).equalTo("documentopedido", codigo+"."+tarea.getDocumento()).findAll().size() + " Fotos";

                            int numFotos = Constants.REALM.where(Foto.class).equalTo("documentopedido", codigo+"."+tarea.getDocumento()).findAll().size();

                            builderTakePhoto.setTitle(codigo);
                            builderTakePhoto.setMessage(msgActualizarPendienteNFotoPromotor);
                            builderTakePhoto.setNegativeButton("Cancelar", null);
                            builderTakePhoto.setNeutralButton("Tomar Foto", null);
                            builderTakePhoto.setPositiveButton("Aceptar", null);

                            final AlertDialog dialogTakePhoto = builderTakePhoto.create();
                            dialogTakePhoto.show();
                            if(numFotos < 1)
                                ((AlertDialog) dialogTakePhoto).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                            Button b_neutral = dialogTakePhoto.getButton(DialogInterface.BUTTON_NEUTRAL);
                            b_neutral.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    CodigoPromotor.codigo = codigo+".";

                                    Intent intent = new Intent(ActualizarPendienteActivity.this, FotoPedidoActivity.class);
                                    intent.putExtra("idpedido", tarea.getIdpedido());
                                    intent.putExtra("documento", tarea.getDocumento());
                                    ActualizarPendienteActivity.this.startActivity(intent);

                                    Log.d("COD1", CodigoPromotor.codigo);

                                    Log.d("COD2", CodigoPromotor.codigo);
                                    TextView tv_message = (TextView) dialogTakePhoto.findViewById(android.R.id.message);
                                    String msgActualizarPendienteNFotoPromotor2 = Constants.REALM.where(Foto.class).equalTo("documentopedido", codigo+"."+tarea.getDocumento()).findAll().size() + " Fotos";
                                    tv_message.setText(msgActualizarPendienteNFotoPromotor2);

                                    ((AlertDialog) dialogTakePhoto).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                                }
                            });
                            Button b_positive = dialogTakePhoto.getButton(DialogInterface.BUTTON_POSITIVE);
                            b_positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String pedido = tarea.getDocumento().substring(0,tarea.getDocumento().indexOf('.'));
                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String fechaActual = formatter.format(date);
                                    Log.d("FECHA", fechaActual);
        //                            System.out.println(formatter.format(date));

                                    AMApiService service = AMApiServiceGenerator.createService(AMApiService.class);
//                                    service.entregaUser(pedido,tarea.getCliente(),USUARIO.getUsuario(),fechaActual,"","","1","Pack Mascarillas y Protectores Faciales",codigo).enqueue(new Callback<ResponseEntregaEspecial>() {
                                    service.entregaUser(pedido,tarea.getCliente(),USUARIO.getUsuario(),fechaActual,"","","1","Pack Mascarillas y Protectores Faciales",codigo).enqueue(new Callback<ResponseEntregaEspecial>() {
                                        @Override
                                        public void onResponse(Call<ResponseEntregaEspecial> call, Response<ResponseEntregaEspecial> response) {
                                            if (response.body().getSuccess()) {
                                                Log.d("SUCCES", response.body().toString());
                                                builderHecho.setTitle(codigo);
                                                builderHecho.setMessage(response.body().getData().getMensaje());
                                                builderHecho.setPositiveButton("Aceptar", null);
                                                AlertDialog dialogHecho = builderHecho.create();
                                                dialogHecho.show();

                                            } else {
        //                                        Log.d("ERROR ENTREGA USER: ", response.body().toString());
                                                builderHecho.setTitle("ERROR");
                                                builderHecho.setMessage(codigo + " no se registro, intentelo nuevamente");
                                                builderHecho.setPositiveButton("Aceptar", null);
                                                AlertDialog dialogHecho = builderHecho.create();
                                                dialogHecho.show();
        //                                Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseEntregaEspecial> call, Throwable t) {
                                            builderHecho.setTitle("ERROR");
                                            builderHecho.setMessage(codigo + " no se registro, intentelo nuevamente");
                                            builderHecho.setPositiveButton("Aceptar", null);
                                            AlertDialog dialogHecho = builderHecho.create();
                                            dialogHecho.show();
                                            Log.d("ERROR ENTREGA: ", "NO SE LLAMO AL SERVICIO");
                                        }
                                    });


                                    CodigoPromotor.codigo = "";
                                    dialogTakePhoto.dismiss();
                                }
                            });

                            Button b_negative = dialogTakePhoto.getButton(DialogInterface.BUTTON_NEGATIVE);
                            b_negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CodigoPromotor.codigo = "";
                                    dialogTakePhoto.dismiss();
                                }
                            });

//                            String pedido = tarea.getDocumento().substring(0,tarea.getDocumento().indexOf('.'));
//                            Date date = new Date();
//                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            String fechaActual = formatter.format(date);
//                            Log.d("FECHA", fechaActual);
////                            System.out.println(formatter.format(date));
//
//                            AMApiService service = AMApiServiceGenerator.createService(AMApiService.class);
////                            service.entregaUser(pedido,tarea.getCliente(),"awb898",formatter.format(date),"","","1","Pack de preuba2",codigo).enqueue(new Callback<ResponseEntregaEspecial>() {
//                            service.entregaUser(pedido,tarea.getCliente(),USUARIO.getUsuario(),fechaActual,"","","1","Pack Mascarillas y Protectores Faciales",codigo).enqueue(new Callback<ResponseEntregaEspecial>() {
//                                @Override
//                                public void onResponse(Call<ResponseEntregaEspecial> call, Response<ResponseEntregaEspecial> response) {
//                                    if (response.body().getSuccess()) {
//                                        Log.d("SUCCES", response.body().toString());
//                                        builderHecho.setTitle(codigo);
//                                        builderHecho.setMessage(response.body().getData().getMensaje());
//                                        builderHecho.setPositiveButton("Aceptar", null);
//                                        AlertDialog dialogHecho = builderHecho.create();
//                                        dialogHecho.show();
//
//                                    } else {
////                                        Log.d("ERROR ENTREGA USER: ", response.body().toString());
//                                        builderHecho.setTitle("ERROR");
//                                        builderHecho.setMessage(codigo + " no se registro, intentelo nuevamente");
//                                        builderHecho.setPositiveButton("Aceptar", null);
//                                        AlertDialog dialogHecho = builderHecho.create();
//                                        dialogHecho.show();
////                                Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseEntregaEspecial> call, Throwable t) {
//                                    builderHecho.setTitle("ERROR");
//                                    builderHecho.setMessage(codigo + " no se registro, intentelo nuevamente");
//                                    builderHecho.setPositiveButton("Aceptar", null);
//                                    AlertDialog dialogHecho = builderHecho.create();
//                                    dialogHecho.show();
//                                    Log.d("ERROR ENTREGA: ", "NO SE LLAMO AL SERVICIO");
//                                }
//                            });
                        }
                    });




                }else{
                    String entregadoMSM = " ya fue entregado";
                    builder.setTitle(codigo+entregadoMSM);
                    builder.setMessage(String.valueOf(dataObject.get("mensaje")));
                    builder.setNegativeButton("Cancelar", null);
                    builder.setPositiveButton("Aceptar", null);
                }
//                builder.setTitle("Titulo");
//                builder.setMessage("Mensaje");
//                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();

//                try {
//                    JSONObject respuesta = new JSONObject(response);
////                    Log.d("REs", respuesta.data);
//                } catch (JSONException e) {
////                    e.printStackTrace();
//                    Log.d("ERROR", response);
//                }


//                Log.d("response", response.toString());
//                Toast.makeText(getApplicationContext(), "Mensaje", Toast.LENGTH_LONG).show();//display the text that you entered in edit text


            }
        },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR CONEXION", Toast.LENGTH_LONG).show();//display the text that you entered in edit text

                //textView.setText("That didn't work!");
            }}

                );

        queue.add(stringRequest);


    }

    private void retornarTarea() {
        if (Constants.REALM.where(Foto.class)
                .equalTo("idpedido", tarea.getIdpedido())
                .equalTo("documentopedido", tarea.getDocumento())
                .findAll().size() == 0) {
            Toast.makeText(this, "Tiene que tomar una foto como mínimo", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarPendienteActivity.this);
            final List<String> motivos = new ArrayList<>();
            estado = Realm.getDefaultInstance().where(Estado.class).equalTo("estado", "Retorno").findFirst();
            for (Motivo motivo : estado.getMotivos()) {
                motivos.add(motivo.getLabel());
            }
            builder.setSingleChoiceItems(motivos.toArray(new String[motivos.size()]), inputSelection,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            inputSelection = item;
                        }
                    });
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.mensaje_retorno_update_tarea, null);
            builder.setView(dialogView);
            final EditText editText = (EditText) dialogView.findViewById(R.id.edit_text);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    guardarEstadoRetorno(editText.getText().toString().replaceAll("\n", " "));
                    Constants.REALM.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            tarea.setLlegada(false);
                        /*realm.where(Tarea.class)
                                .equalTo("documento", tarea.getDocumento())
                                .equalTo("idpedido", tarea.getIdpedido())
                                .findFirst().setLlegada(false);*/
                        }
                    });
                    Intent intent = new Intent(ActualizarPendienteActivity.this, TareasActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    dialog.dismiss();
                    ActualizarPendienteActivity.this.startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
        }
    }

    private void guardarEstadoRetorno(String comentario) {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final RegEstado regEstado = new RegEstado();
        regEstado.setDocumento(tarea.getDocumento());
        regEstado.setUsuario(Constants.USUARIO.getUsuario());
        regEstado.setUsuario_id(Constants.USUARIO.getId());
        regEstado.setEstado(estado.getEstado());
        regEstado.setMotivo(estado.getMotivos().get(inputSelection).getLabel());
        regEstado.setCobranza("");
        regEstado.setFecha(dateFormat.format(currentTime));
        regEstado.setLatitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLatitude()));
        regEstado.setLongitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLongitude()));
        regEstado.setComentario(comentario);
        regEstado.setCod_estado(estado.getMotivos().get(inputSelection).getCodigo());
        regEstado.setCod_motivo(estado.getMotivos().get(inputSelection).getCodigo());
        regEstado.setCod_cobranza("");
        List<RegEstado> regEstadoList = new ArrayList<>();
        regEstadoList.add(regEstado);
        HashMap<String, List<RegEstado>> map = new HashMap<>();
        map.put("data", regEstadoList);
        Gson gson = new Gson();
        Log.d(Constants.TAG, "onResponseGuardarEstado: " + gson.toJson(map));
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

    private void guardarEstadoInicio() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final RegEstado regEstado = new RegEstado();
        regEstado.setDocumento(tarea.getDocumento());
        regEstado.setUsuario(Constants.USUARIO.getUsuario());
        regEstado.setUsuario_id(Constants.USUARIO.getId());
        regEstado.setEstado("Inicio");
        regEstado.setMotivo("Inicio");
        regEstado.setCobranza("");
        regEstado.setFecha(dateFormat.format(currentTime));
        regEstado.setLatitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLatitude()));
        regEstado.setLongitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLongitude()));
        regEstado.setComentario("");
        regEstado.setCod_estado("EST0201");
        regEstado.setCod_motivo("EST0201");
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
