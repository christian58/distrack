package com.academiamoviles.distrack.services;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.models.CodigoPromotor;
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
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.academiamoviles.distrack.Constants.REALM;
import static com.academiamoviles.distrack.Constants.TAG;
import static com.academiamoviles.distrack.Constants.tarea;

//import static  com.academiamoviles.distrack.activities.FotoPedidoActivity.mCurrentPhotoPat
public class AMCamera {

    public static String mCurrentPhotoPath;

    public static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public static void takePhoto(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, Constants.PERMISSIONS_LIST_CAMERA, Constants.CAPTURE_IMAGE_REQUEST);
        } else {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                // Create the File where the photo should go
//                activity.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), Constants.CAPTURE_IMAGE_REQUEST); // original
                File photoFile = null;
                try {
                    photoFile = createImageFile(activity);
                } catch (IOException ex) {

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(activity,
                            "com.academiamoviles.distrack.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    activity.startActivityForResult(takePictureIntent, Constants.CAPTURE_IMAGE_REQUEST);
                }
            }

//            activity.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), Constants.CAPTURE_IMAGE_REQUEST);
        }
    }

    public static void guardarFoto(Bitmap bmp, final String id, double longitud, double latitud) throws IOException {
        File folder = new File(Constants.FOTO_DIRECTORY);
        Log.d("GUARDAR", "guardarFoto: ");
        Log.d("PORFA", tarea.getDocumento());
        Log.d("PORFA", tarea.getAux5());
        Log.d("PROMOTOR", CodigoPromotor.codigo);



        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Date currentTime = Calendar.getInstance().getTime();
            DateFormat hourFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            final String timeStamp = tarea.getDocumento() + "_" + hourFormat.format(currentTime);
            final File image = new File(Constants.FOTO_DIRECTORY, timeStamp + ".jpg");
            Bitmap imagenFinal = getResizedBitmap(bmp, 1200);
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Foto foto = realm.createObject(Foto.class);
                    foto.setNombre(timeStamp + ".jpg");
                    foto.setUrl(image.getPath());
                    foto.setIdpedido(id);
                    foto.setDocumentopedido(tarea.getDocumento());
                }
            });
            FileOutputStream fo = new FileOutputStream(image);
            imagenFinal.compress(Bitmap.CompressFormat.JPEG, 50, fo);
            fo.close();
            subirFoto(image, bmp, currentTime, tarea, latitud, longitud);
//            subirFoto(image, imagenFinal, currentTime, tarea, latitud, longitud);
        }
    }

    private static void subirFoto(final File image, Bitmap imageBitmap, Date currentTime, Tarea tarea, double latitude, double longitude) {
        Log.d("PROMOTOR", CodigoPromotor.codigo);
        DateFormat hourFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        final String timeStamp = hourFormat.format(currentTime);
        DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("fileImage", timeStamp + ".jpg", requestFile);
        RequestBody idusuario = RequestBody.create(MultipartBody.FORM, String.valueOf(Constants.USUARIO.getIdusuario()));
        RequestBody usuario = RequestBody.create(MultipartBody.FORM, Constants.USUARIO.getUsuario());
        RequestBody idpedido = RequestBody.create(MultipartBody.FORM, CodigoPromotor.codigo + tarea.getIdpedido());
        RequestBody documento = RequestBody.create(MultipartBody.FORM, CodigoPromotor.codigo + tarea.getDocumento());
        RequestBody estado = RequestBody.create(MultipartBody.FORM, "");
        RequestBody fecha = RequestBody.create(MultipartBody.FORM, fechaFormat.format(currentTime));
        RequestBody hora = RequestBody.create(MultipartBody.FORM, horaFormat.format(currentTime));
        RequestBody latitud = RequestBody.create(MultipartBody.FORM, String.valueOf(latitude));
        RequestBody longitud = RequestBody.create(MultipartBody.FORM, String.valueOf(longitude));
        Constants.APISEVICE.sendFotoPedido(fileImage, idusuario, usuario, idpedido, documento, estado, fecha, hora, latitud, longitud)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponseFotoEnviado1: " + response.toString());
                        Log.d(TAG, "onResponseFotoEnviado2: " + response);
                        Log.d(TAG, "onResponseFotoEnviado3: " + response.message());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Constants.REALM.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.where(Foto.class).equalTo("url", image.getPath()).findFirst().setSincronizar(true);
                            }
                        });
                    }
                });
    }

    private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static void subirDatosTarea(boolean finalizado, final Tarea tarea, double latitude, double longitude) {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DateFormat horaFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        RequestBody idusuario = RequestBody.create(MultipartBody.FORM, String.valueOf(Constants.USUARIO.getIdusuario()));
        RequestBody usuario = RequestBody.create(MultipartBody.FORM, Constants.USUARIO.getUsuario());
        RequestBody estado = RequestBody.create(MultipartBody.FORM, "");
        RequestBody idpedido = RequestBody.create(MultipartBody.FORM, tarea.getIdpedido());
        RequestBody documento = RequestBody.create(MultipartBody.FORM, tarea.getDocumento());
        RequestBody fecha = RequestBody.create(MultipartBody.FORM, fechaFormat.format(currentTime));
        RequestBody hora = RequestBody.create(MultipartBody.FORM, horaFormat.format(currentTime));
        RequestBody latitud = RequestBody.create(MultipartBody.FORM, String.valueOf(latitude));
        RequestBody longitud = RequestBody.create(MultipartBody.FORM, String.valueOf(longitude));
        RequestBody requestFile;
        RequestBody indice;
        MultipartBody.Part fileImage;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap imageBitmap;
        byte[] byteArray;
        List<Foto> fotosTarea = Constants.REALM.where(Foto.class)
                .equalTo("documentopedido", tarea.getDocumento())
                .equalTo("idpedido", tarea.getIdpedido())
                .findAll();
        List<Firma> firmasTarea = Constants.REALM.where(Firma.class)
                .equalTo("documentopedido", tarea.getDocumento())
                .findAll();
        for (final Foto foto : fotosTarea) {
            File file = new File(foto.getUrl());
            if (file.exists()) {
                Log.d(TAG, "subirFotosTarea: existe");
                if (foto.getSincronizar()) {
                    imageBitmap = BitmapFactory.decodeFile(foto.getUrl());
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();
                    requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
                    fileImage = MultipartBody.Part.createFormData("fileImage", foto.getNombre(), requestFile);
                    Constants.APISEVICE.sendFotoPedido(fileImage, idusuario, usuario, idpedido, documento, estado, fecha, hora, latitud, longitud).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d(TAG, "onResponseFotoEnviado1: " + response.toString());
                            Log.d(TAG, "onResponseFotoEnviado2: " + response);
                            Log.d(TAG, "onResponseFotoEnviado3: " + response.message());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
            if (tarea.getSincronizar() || finalizado) {
                file.delete();
                REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG, "execute: fotoEliminida: " + foto.getNombre());
                        foto.deleteFromRealm();
                    }
                });
            }
        }
        for (final Firma firma : firmasTarea) {
            File file = new File(firma.getUrl());
            if (file.exists()) {
                Log.d(TAG, "subirFotosTarea: existe");
                if (firma.getSincronizar()) {
                    indice = RequestBody.create(MultipartBody.FORM, "");
                    imageBitmap = BitmapFactory.decodeFile(firma.getUrl());
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                    requestFile = RequestBody.create(MediaType.parse("image/png"), byteArray);
                    fileImage = MultipartBody.Part.createFormData("fileImage", firma.getNombre(), requestFile);
                    Constants.APISEVICE.sendFirmaPedido(fileImage, idusuario, indice, documento, usuario, estado, fecha, hora, latitud, longitud).enqueue(new Callback<ResponseFirma>() {
                        @Override
                        public void onResponse(Call<ResponseFirma> call, Response<ResponseFirma> response) {
                            Log.d(Constants.TAG, "onResponseFirmaEnviado4: " + response.body().getSuccess());
                        }

                        @Override
                        public void onFailure(Call<ResponseFirma> call, Throwable t) {

                        }
                    });
                }
            }
            if (tarea.getSincronizar() || finalizado) {
                file.delete();
                REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d(TAG, "execute: firmaEliminida: " + firma.getNombre());
                        firma.deleteFromRealm();
                    }
                });
            }
        }
        if (tarea.getSincronizar() || finalizado) {
            Constants.REALM.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Log.d(TAG, "execute: tareaEliminada: " + tarea.getDocumento());
                    realm.where(Tarea.class)
                            .equalTo("idpedido", tarea.getIdpedido())
                            .equalTo("documento", tarea.getDocumento())
                            .findFirst().deleteFromRealm();

                }
            });
        }
    }

}
