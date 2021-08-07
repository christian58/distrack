package com.academiamoviles.distrack.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.academiamoviles.distrack.BuildConfig;
import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.models.Estado;
import com.academiamoviles.distrack.models.EstadoCobranza;
import com.academiamoviles.distrack.models.ResponseEstadoCobranzaList;
import com.academiamoviles.distrack.models.ResponseEstadoList;
import com.academiamoviles.distrack.models.ResponseLogin;
import com.academiamoviles.distrack.models.ResponsePedidosPendientes;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMLocationManager;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Realm realm;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        if (realm.where(User.class).findFirst() != null) {
            startActivity(new Intent(LoginActivity.this, TareasActivity.class));
            finish();
        }
        String version = "Versión: " + BuildConfig.VERSION_NAME;
        ((TextView) findViewById(R.id.loginVersion)).setText(version);
        findViewById(R.id.loginAyuda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file2 = new File(getFilesDir(), Constants.PDF_DIRECTORY);
                if (!file2.exists()) {
                    AssetManager assets = getAssets();
                    try {
                        copy(assets.open(Constants.PDF_DIRECTORY), file2);
                    } catch (IOException e) {
                        Log.e("FileProvider", "Exception copying from assets", e);
                    }
                }
                if (file2.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri contentUri = FileProvider.getUriForFile(LoginActivity.this, "com.academiamoviles.distrack.fileprovider", file2);
                    intent.setDataAndType(contentUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(LoginActivity.this,
                                "No tiene una aplicación instalada para ver el PDF",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AMLocationManager.getInstance().setActivity(this);
        if (AMLocationManager.getInstance().statusCheck()) {
            if (!AMLocationManager.getInstance().isInit()) {
                AMLocationManager.getInstance().init();
                AMLocationManager.getInstance().setInit(true);
            }
            AMLocationManager.getInstance().checkLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.UBICACION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, " Permiso otorgado", Toast.LENGTH_SHORT).show();
                    AMLocationManager.getInstance().init();
                    AMLocationManager.getInstance().setInit(true);
                } else {
                    Toast.makeText(this, " Permiso rechazado", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    public void loginUser(View view) {
        Log.d("haciendo LGOIN: ", "LOGEANDO...");
        if (isOnline()) {
            Log.d("haciendo LGOIN: ", "is ONLINE...");
            AMApiService service = AMApiServiceGenerator.createService(AMApiService.class);
            String loginUsuario = ((EditText) findViewById(R.id.loginUsuario)).getText().toString();
            String loginClave = ((EditText) findViewById(R.id.loginClave)).getText().toString();
            service.loginUser(loginUsuario, loginClave,
                    String.valueOf(AMLocationManager.getInstance().getLastLocation().getLatitude()),
                    String.valueOf(AMLocationManager.getInstance().getLastLocation().getLongitude()))
                    .enqueue(new Callback<ResponseLogin>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseLogin> call, @NonNull Response<ResponseLogin> response) {
                            Log.d(Constants.TAG, "onResponse: " + response.toString());
                            if (response.body().getSuccess()) {
                                progress = new ProgressDialog(LoginActivity.this);
                                progress.setMessage("Actualizando datos...");
                                progress.setCancelable(false);
                                progress.setCanceledOnTouchOutside(false);
                                progress.show();
                                final User user = response.body().getUser();
                                Constants.USUARIO = user;
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealmOrUpdate(user);
                                    }
                                });
                                actualizarDatos();
                            } else {
                                Log.d("ERROR LOGIN: ", response.body().toString());
                                Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseLogin> call, Throwable t) {
                            Log.d("ERROR LOGIN: ", "NO SE LLAMO AL SERVICIO");
                            Toast.makeText(LoginActivity.this, "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.d("haciendo LGOIN: ", "NO ONLINE...");
            Toast.makeText(this, "Compruebe su conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void copy(InputStream in, File dst) throws IOException {
        FileOutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    private void actualizarDatos() {
        AMApiService amApiService = AMApiServiceGenerator.createService(AMApiService.class);
        amApiService.getEstadoList().enqueue(new Callback<ResponseEstadoList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEstadoList> call, @NonNull Response<ResponseEstadoList> response) {
                ResponseEstadoList responseEstadoList = response.body();
                for (int i = 0; i < responseEstadoList.getData().size(); i++) {
                    final Estado estado = responseEstadoList.getData().get(i);
                    estado.setId(i);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            realm.copyToRealmOrUpdate(estado);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseEstadoList> call, @NonNull Throwable t) {

            }
        });
        amApiService.getEstadoCobranzaList().enqueue(new Callback<ResponseEstadoCobranzaList>() {
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

            }
        });
        amApiService.getPedidos(Realm.getDefaultInstance().where(User.class).findFirst().getUsuario())
                .enqueue(new Callback<ResponsePedidosPendientes>() {
                    @Override
                    public void onResponse(Call<ResponsePedidosPendientes> call, Response<ResponsePedidosPendientes> response) {
                        ResponsePedidosPendientes responsePedidosPendientes = response.body();
                        for (int i = 0; i < responsePedidosPendientes.getData().size(); i++) {
                            final Tarea tarea = responsePedidosPendientes.getData().get(i);
                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(@NonNull Realm realm) {
                                    if (realm.where(Tarea.class)
                                            .equalTo("documento", tarea.getDocumento())
                                            .findFirst() == null) {
                                        realm.copyToRealm(tarea);
                                    }
                                }
                            });
                        }
                        progress.dismiss();
                        startActivity(new Intent(LoginActivity.this, TareasActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponsePedidosPendientes> call, Throwable t) {

                    }
                });
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
