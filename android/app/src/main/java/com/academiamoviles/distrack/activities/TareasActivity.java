package com.academiamoviles.distrack.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.adapters.PagerTareasAdapter;
import com.academiamoviles.distrack.models.Estado;
import com.academiamoviles.distrack.models.EstadoCobranza;
import com.academiamoviles.distrack.models.Firma;
import com.academiamoviles.distrack.models.Foto;
import com.academiamoviles.distrack.models.Motivo;
import com.academiamoviles.distrack.models.RegEstado;
import com.academiamoviles.distrack.models.ResponseEstadoCobranzaList;
import com.academiamoviles.distrack.models.ResponseEstadoList;
import com.academiamoviles.distrack.models.ResponseFirma;
import com.academiamoviles.distrack.models.ResponsePedidosPendientes;
import com.academiamoviles.distrack.models.ResponseRegEstado;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;
import com.academiamoviles.distrack.services.AMCamera;
import com.academiamoviles.distrack.services.AMDrawManager;
import com.academiamoviles.distrack.services.AMLocationManager;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
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

import static com.academiamoviles.distrack.Constants.TAG;


public class TareasActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private Calendar calendarPasado;
    private Calendar calendarHoy;
    private DateFormat dateFormat;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (Realm.getDefaultInstance() == null) {
            Realm.init(this);
        }
        Constants.REALM = Realm.getDefaultInstance();
        Constants.USUARIO = Constants.REALM.where(User.class).findFirst();
        Constants.APISEVICE = AMApiServiceGenerator.createService(AMApiService.class);

        calendarPasado = Calendar.getInstance();
        calendarHoy = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        //SharedPreferences.Editor editor2 = sharedPref.edit();
        //editor2.putString("fecha", "2016-11-28");
        //editor2.commit();

        String fPasado = sharedPref.getString("fecha", "1");
        if (fPasado.equals("1")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            fPasado = dateFormat.format(calendarHoy.getTime());
            editor.putString("fecha", dateFormat.format(calendarHoy.getTime()));
            editor.commit();
        }
        try {
            calendarPasado.setTime(dateFormat.parse(fPasado));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //actualizarDatos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);

        RealmResults<Tarea> tareasPendientes = Constants.REALM.where(Tarea.class).equalTo("sincronizar", false).findAll();
        RealmResults<Tarea> tareasSincronizar = Constants.REALM.where(Tarea.class).equalTo("sincronizar", true).findAll();

        int nPendientes = tareasPendientes.size();
        int nSincronizar = tareasSincronizar.size();

        PagerTareasAdapter adapter = new PagerTareasAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendientesFragment(), "Pendientes (" + nPendientes + ")");
        adapter.addFragment(new SincronizarFragment(), "Por Sincronizar (" + nSincronizar + ")");
        viewPager.setAdapter(adapter);
        AMLocationManager.getInstance().setActivity(this);
        if (AMLocationManager.getInstance().statusCheck()) {
            if (!AMLocationManager.getInstance().isInit()) {
                AMLocationManager.getInstance().init();
                AMLocationManager.getInstance().setInit(true);
            }
            if (calendarPasado.get(Calendar.DAY_OF_MONTH) < calendarHoy.get(Calendar.DAY_OF_MONTH) ||
                    calendarPasado.get(Calendar.MONTH) < calendarHoy.get(Calendar.MONTH) ||
                    calendarPasado.get(Calendar.YEAR) < calendarHoy.get(Calendar.YEAR)) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("fecha", dateFormat.format(calendarHoy.getTime()));
                editor.apply();
                AMLocationManager.getInstance().checkLastLocation2(this);
            } else {
                AMLocationManager.getInstance().checkLastLocation();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_tareas:
                actualizarDatos();
                return true;
            case R.id.logout:
                mensajeSalida();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void actualizarDatos() {
        if (isOnline()) {
            progress = new ProgressDialog(TareasActivity.this);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("Actualizando datos...");
            progress.show();
            subirRegistros();
            Constants.APISEVICE.getEstadoList().enqueue(new Callback<ResponseEstadoList>() {
                @Override
                public void onResponse(@NonNull Call<ResponseEstadoList> call, @NonNull Response<ResponseEstadoList> response) {
                    ResponseEstadoList responseEstadoList = response.body();
                    for (int i = 0; i < responseEstadoList.getData().size(); i++) {
                        final Estado estado = responseEstadoList.getData().get(i);
                        estado.setId(i);
                        Constants.REALM.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NonNull Realm realm) {
                                realm.copyToRealmOrUpdate(estado);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseEstadoList> call, @NonNull Throwable t) {
                    if (progress.isShowing()) {
                        Toast.makeText(TareasActivity.this, "No se pudo actualizar los datos, compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    Log.e(TAG, "onFailure: ", t);
                }
            });
            List<Tarea> listaTareas = Constants.REALM.where(Tarea.class).findAll();
            for (Tarea tarea : listaTareas) {
                AMCamera.subirDatosTarea(false, tarea,
                        AMLocationManager.getInstance().getLastLocation().getLatitude(),
                        AMLocationManager.getInstance().getLastLocation().getLongitude());

            }
            Constants.APISEVICE.getEstadoCobranzaList().enqueue(new Callback<ResponseEstadoCobranzaList>() {
                @Override
                public void onResponse(@NonNull Call<ResponseEstadoCobranzaList> call, @NonNull Response<ResponseEstadoCobranzaList> response) {
                    ResponseEstadoCobranzaList responseEstadoCobranzaList = response.body();
                    for (int i = 0; i < responseEstadoCobranzaList.getData().size(); i++) {
                        final EstadoCobranza estadoCobranza = responseEstadoCobranzaList.getData().get(i);
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NonNull Realm realm) {
                                realm.copyToRealmOrUpdate(estadoCobranza);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseEstadoCobranzaList> call, @NonNull Throwable t) {
                    if (progress.isShowing()) {
                        Toast.makeText(TareasActivity.this, "No se pudo actualizar los datos, compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                }
            });
            Constants.APISEVICE.getPedidos(Constants.USUARIO.getUsuario())
                    .enqueue(new Callback<ResponsePedidosPendientes>() {
                        @Override
                        public void onResponse(Call<ResponsePedidosPendientes> call, Response<ResponsePedidosPendientes> response) {
                            ResponsePedidosPendientes responsePedidosPendientes = response.body();
                            List<Tarea> tareasNuevas = responsePedidosPendientes.getData();
                            for (final Tarea tarea : tareasNuevas) {
                                Tarea tareaAnterior = Constants.REALM.where(Tarea.class).equalTo("documento", tarea.getDocumento()).findFirst();
                                if (tareaAnterior != null) {
                                    tarea.setLlegada(tareaAnterior.getLlegada());
                                    tarea.setIniciado1(tareaAnterior.getIniciado1());
                                    tarea.setIniciado2(tareaAnterior.getIniciado2());
                                    tarea.setTareaEstado(tareaAnterior.getTareaEstado());
                                    tarea.setTareaCodEstado(tareaAnterior.getTareaCodEstado());
                                    tarea.setTareaMotivo(tareaAnterior.getTareaMotivo());
                                    tarea.setTareaCodMotivo(tareaAnterior.getTareaCodMotivo());
                                    tarea.setComentario(tareaAnterior.getComentario());
                                }
                                tarea.setEncontrado(true);
                                Constants.REALM.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealmOrUpdate(tarea);
                                    }
                                });
                            }
                            List<Tarea> tareaNoEncontradas = Constants.REALM.where(Tarea.class).equalTo("encontrado", false).findAll();
                            for (final Tarea tarea : tareaNoEncontradas) {
                                AMCamera.subirDatosTarea(true, tarea,
                                        AMLocationManager.getInstance().getLastLocation().getLatitude(),
                                        AMLocationManager.getInstance().getLastLocation().getLongitude());
                            }
                            List<Tarea> tareaEncontradas = Constants.REALM.where(Tarea.class).equalTo("encontrado", true).findAll();
                            for (final Tarea tarea : tareaEncontradas) {
                                Constants.REALM.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        tarea.setEncontrado(false);
                                        realm.copyToRealmOrUpdate(tarea);
                                    }
                                });
                            }
                            Constants.REALM.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(RegEstado.class).findAll().deleteAllFromRealm();
                                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                                    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                                    tabLayout.setupWithViewPager(viewPager);
                                    int nPendientes = Constants.REALM.where(Tarea.class).equalTo("sincronizar", false).findAll().size();
                                    int nSincronizar = Constants.REALM.where(Tarea.class).equalTo("sincronizar", true).findAll().size();
                                    PagerTareasAdapter adapter = new PagerTareasAdapter(getSupportFragmentManager());
                                    adapter.addFragment(new PendientesFragment(), "Pendientes (" + nPendientes + ")");
                                    adapter.addFragment(new SincronizarFragment(), "Por Sincronizar (" + nSincronizar + ")");
                                    viewPager.setAdapter(adapter);
                                }
                            });
                            progress.dismiss();
                            Toast.makeText(TareasActivity.this, "Datos Actualizados", Toast.LENGTH_LONG).show();
                            //Toast.makeText(TareasActivity.this, "Datos Actualizados", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Call<ResponsePedidosPendientes> call, Throwable t) {
                            if (progress.isShowing()) {
                                Toast.makeText(TareasActivity.this, "No se pudo actualizar los datos, compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void mensajeSalida() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Constants.MENSAJE_SALIDACONFIRMACION);
        builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                progress = new ProgressDialog(TareasActivity.this);
                progress.setCancelable(false);
                progress.setCanceledOnTouchOutside(false);
                progress.setMessage("Actualizando datos...");
                progress.show();
                Constants.APISEVICE.logoutUser(Constants.USUARIO.getIdusuario(),
                        Constants.USUARIO.getUsuario(),
                        AMLocationManager.getInstance().getLastLocation().getLatitude(),
                        AMLocationManager.getInstance().getLastLocation().getLongitude())
                        .enqueue(new Callback<ResponseFirma>() {
                            @Override
                            public void onResponse(Call<ResponseFirma> call, Response<ResponseFirma> response) {
                                Log.d(Constants.TAG, "onResponse: " + response.body().getSuccess());
                                subirRegistros();
                                List<Tarea> listaTareas = Constants.REALM.where(Tarea.class).findAll();
                                for (Tarea tarea : listaTareas) {
                                    AMCamera.subirDatosTarea(true, tarea,
                                            AMLocationManager.getInstance().getLastLocation().getLatitude(),
                                            AMLocationManager.getInstance().getLastLocation().getLongitude());
                                }
                                Constants.REALM.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(@NonNull Realm realm) {
                                        realm.deleteAll();
                                    }
                                });
                                progress.dismiss();
                                dialog.dismiss();
                                startActivity(new Intent(TareasActivity.this, LoginActivity.class));
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseFirma> call, Throwable t) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(TareasActivity.this);
                                builder.setMessage(Constants.MENSAJE_SALIDACONFIRMACIONSININTERNET);
                                builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        List<Foto> fotos = Constants.REALM.where(Foto.class).findAll();
                                        List<Firma> firmas = Constants.REALM.where(Firma.class).findAll();
                                        for (Foto foto : fotos) {
                                            File file = new File(foto.getUrl());
                                            if (file.exists()) {
                                                file.delete();
                                            }
                                        }
                                        for (Firma firma : firmas) {
                                            File file = new File(firma.getUrl());
                                            if (file.exists()) {
                                                file.delete();
                                            }
                                        }
                                        Constants.REALM.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(@NonNull Realm realm) {
                                                realm.deleteAll();
                                            }
                                        });
                                        progress.dismiss();
                                        dialog.dismiss();
                                        startActivity(new Intent(TareasActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progress.dismiss();
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                            }
                        });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void subirRegistros() {
        List<RegEstado> regEstadoList = Constants.REALM.where(RegEstado.class).findAll();
        for (int i = 0; i < regEstadoList.size(); i++) {
            final RegEstado regEstado = new RegEstado();
            regEstado.setDocumento(regEstadoList.get(i).getDocumento());
            regEstado.setUsuario(regEstadoList.get(i).getUsuario());
            regEstado.setUsuario_id(regEstadoList.get(i).getUsuario_id());
            regEstado.setEstado(regEstadoList.get(i).getEstado());
            regEstado.setMotivo(regEstadoList.get(i).getMotivo());
            regEstado.setCobranza(regEstadoList.get(i).getCobranza());
            regEstado.setFecha(regEstadoList.get(i).getFecha());
            regEstado.setLatitud(regEstadoList.get(i).getLatitud());
            regEstado.setLongitud(regEstadoList.get(i).getLongitud());
            regEstado.setComentario(regEstadoList.get(i).getComentario());
            regEstado.setCod_estado(regEstadoList.get(i).getCod_estado());
            regEstado.setCod_motivo(regEstadoList.get(i).getCod_motivo());
            regEstado.setCod_cobranza(regEstadoList.get(i).getCod_cobranza());
            HashMap<String, List<RegEstado>> map = new HashMap<>();
            List<RegEstado> regEstadoList1 = new ArrayList<>();
            regEstadoList1.add(regEstado);
            map.put("data", regEstadoList1);
            Constants.APISEVICE.sendRegEstado(map).enqueue(new Callback<List<ResponseRegEstado>>() {
                @Override
                public void onResponse(Call<List<ResponseRegEstado>> call, Response<List<ResponseRegEstado>> response) {
                    Log.d(Constants.TAG, "onResponseGuardarEstado: " + response.body().get(0).getSuccess());
                }

                @Override
                public void onFailure(Call<List<ResponseRegEstado>> call, Throwable t) {
                    if (progress != null) {
                        if (progress.isShowing()) {
                            Toast.makeText(TareasActivity.this, "No se pudo actualizar los datos, compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                    Log.d(Constants.TAG, "onFailure: " + t.toString());
                }
            });
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
