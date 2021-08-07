package com.academiamoviles.distrack.services;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.models.Firma;
import com.academiamoviles.distrack.models.Foto;
import com.academiamoviles.distrack.models.ResponseFirma;
import com.academiamoviles.distrack.models.Tarea;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.academiamoviles.distrack.Constants.REALM;
import static com.academiamoviles.distrack.Constants.tarea;

public class AMDrawManager extends AppCompatActivity implements View.OnClickListener {
    private GestureOverlayView gesture;
    ImageView iv;
    private Firma firma;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amdrawmanager);
        iv = (ImageView) findViewById(R.id.iv);
        gesture = (GestureOverlayView) findViewById(R.id.gestures);
        //gesture.setFadeEnabled(false);
        gesture.setFadeOffset(60 * 60 * 1000);
        findViewById(R.id.btnGuardar).setOnClickListener(this);
        findViewById(R.id.btnLimpiar).setOnClickListener(this);

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (Constants.REALM.where(Firma.class).equalTo("documentopedido", tarea.getDocumento()).findAll().size() > 0) {
            firma = Constants.REALM.where(Firma.class)
                    .equalTo("documentopedido", tarea.getDocumento())
                    .findAllSorted("nombre", Sort.DESCENDING).first();
            File f = new File(firma.getUrl());
            if (f.exists()) {
                iv.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
                iv.setVisibility(View.VISIBLE);
                gesture.setVisibility(View.GONE);
            } else {
                iv.setVisibility(View.INVISIBLE);
                gesture.setVisibility(View.VISIBLE);
            }
        } else {
            iv.setVisibility(View.INVISIBLE);
            gesture.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLimpiar:

                iv.setVisibility(View.GONE);
                gesture.setVisibility(View.VISIBLE);
                gesture.cancelClearAnimation();
                gesture.clear(true);
                break;

            case R.id.btnGuardar:
                if (ActivityCompat.checkSelfPermission(AMDrawManager.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(AMDrawManager.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AMDrawManager.this, Constants.PERMISSIONS_LIST_CAMERA, Constants.CAPTURE_IMAGE_REQUEST);
                } else {
                    if (gesture.getGesture() != null) {
                        final Firma firmaAnterior = Constants.REALM.where(Firma.class).equalTo("documentopedido", tarea.getDocumento()).findFirst();
                        if (firmaAnterior != null) {
                            File fileAnterior = new File(firmaAnterior.getUrl());
                            if (fileAnterior.exists()) {
                                fileAnterior.delete();
                            }
                            Constants.REALM.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    firmaAnterior.deleteFromRealm();
                                }
                            });
                        }
                        Bitmap gestureImg = gesture.getGesture().toBitmap(iv.getWidth(), iv.getHeight(), 30, Color.BLACK);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        gestureImg.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] bArray = bos.toByteArray();
                        Date currentTime = Calendar.getInstance().getTime();
                        DateFormat hourFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                        final String timeStamp = tarea.getDocumento() +
                                "_" + hourFormat.format(currentTime);
                        final File f = new File(Constants.FOTO_DIRECTORY, timeStamp + ".png");
                        Constants.REALM.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Firma firma = realm.createObject(Firma.class);
                                firma.setNombre(timeStamp + ".png");
                                firma.setUrl(f.getPath());
                                firma.setDocumentopedido(tarea.getDocumento());
                            }
                        });

                        subirFirma(timeStamp + ".png", f, gestureImg, AMLocationManager.getInstance().getLastLocation().getLatitude(), AMLocationManager.getInstance().getLastLocation().getLongitude());
                        //File f = new File(getApplicationContext().getCacheDir(), "firma_" + tarea.getDocumento());
                        try {
                            f.createNewFile();
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bArray);
                            fos.flush();
                            fos.close();
                            Toast.makeText(getApplicationContext(), "Firma almacenada correctamente.", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Error al guardar la firma.", Toast.LENGTH_SHORT).show();
                            Log.e("-", ">" + e.getMessage());
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Ingrese su firma.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void subirFirma(String nombre, final File firma, Bitmap gestureImg, double latitude, double longitude) {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        gestureImg.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), bArray);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("fileImage", nombre, requestFile);
        RequestBody idusuario = RequestBody.create(MultipartBody.FORM, String.valueOf(Constants.USUARIO.getIdusuario()));
        RequestBody indice = RequestBody.create(MultipartBody.FORM, "1");
        RequestBody documento = RequestBody.create(MultipartBody.FORM, tarea.getDocumento());
        RequestBody usuario = RequestBody.create(MultipartBody.FORM, Constants.USUARIO.getUsuario());
        RequestBody estado = RequestBody.create(MultipartBody.FORM, "");
        RequestBody fecha = RequestBody.create(MultipartBody.FORM, fechaFormat.format(currentTime));
        RequestBody hora = RequestBody.create(MultipartBody.FORM, horaFormat.format(currentTime));
        RequestBody latitud = RequestBody.create(MultipartBody.FORM, String.valueOf(latitude));
        RequestBody longitud = RequestBody.create(MultipartBody.FORM, String.valueOf(longitude));
        Log.d(Constants.TAG, "onResponseFirmaEnviado4: " + tarea.getDocumento());
        Log.d(Constants.TAG, "onResponseFirmaEnviado4: " + nombre);
        Constants.APISEVICE.sendFirmaPedido(fileImage, idusuario, indice, documento, usuario, estado, fecha, hora, latitud, longitud).enqueue(new Callback<ResponseFirma>() {
            @Override
            public void onResponse(Call<ResponseFirma> call, Response<ResponseFirma> response) {
                Log.d(Constants.TAG, "onResponseFirmaEnviado4: " + response.body().getSuccess());
                Log.d(Constants.TAG, "onResponseFirmaEnviado4: " + response.toString());
            }

            @Override
            public void onFailure(Call<ResponseFirma> call, Throwable t) {
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Firma.class).equalTo("url", firma.getPath()).findFirst().setSincronizar(true);
                    }
                });
            }
        });
    }

}
