package com.academiamoviles.distrack.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.adapters.FotosTareaAdapter;
import com.academiamoviles.distrack.models.Foto;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;
import com.academiamoviles.distrack.services.AMCamera;
import com.academiamoviles.distrack.services.AMLocationManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class FotoPedidoActivity extends AppCompatActivity {

    private Tarea tarea;
    private int nFotos;
    private double latitude;
    private double longitude;
    private RecyclerView recyclerView;
    private FotosTareaAdapter fotosTareaAdapter;
    private FloatingActionButton tomarFoto;

    public static String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_pedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Bundle data = getIntent().getExtras();

        if (Constants.REALM == null) {
            Realm.init(this);
            Constants.REALM = Realm.getDefaultInstance();
            Constants.USUARIO = Constants.REALM.where(User.class).findFirst();
            Constants.APISEVICE = AMApiServiceGenerator.createService(AMApiService.class);
        }
        tarea = Constants.REALM.where(Tarea.class)
                .equalTo("idpedido", data.getString("idpedido"))
                .equalTo("documento", data.getString("documento"))
                .findFirst();
        RealmResults<Foto> fotos = Constants.REALM.where(Foto.class)
                .equalTo("idpedido", tarea.getIdpedido())
                .equalTo("documentopedido", tarea.getDocumento())
                .findAll();
        if (fotos.size() == 0) {
            AMCamera.takePhoto(FotoPedidoActivity.this);
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        recyclerView = (RecyclerView) findViewById(R.id.listaFotos);
        tomarFoto = (FloatingActionButton) findViewById(R.id.tomarFoto);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        fotosTareaAdapter = new FotosTareaAdapter(getApplicationContext(), fotos);
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
        RealmResults<Foto> fotos = Constants.REALM.where(Foto.class)
                .equalTo("idpedido", tarea.getIdpedido())
                .equalTo("documentopedido", tarea.getDocumento())
                .findAll();
        Log.d("onRESUME SIZE", String.valueOf(fotos.size()));
        nFotos = fotos.size();
        if (nFotos < Constants.USUARIO.getMax_foto()) {
            Log.d(Constants.TAG, "onResume2: " + nFotos);
            tomarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AMCamera.takePhoto(FotoPedidoActivity.this);
                }
            });
        } else {
            tomarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
        Log.d(Constants.TAG, "onResume1: " + nFotos);
        Log.d(Constants.TAG, "onResume3: " + Constants.USUARIO.getMax_foto());
        fotosTareaAdapter.setFotos(fotos);
        recyclerView.setAdapter(fotosTareaAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        try {
//            Log.d("YA", "onActivityResult: ");
//            switch (requestCode) {
//                case 0: {
//                    Log.d("YA0", "onActivityResult: ");
//                    if (resultCode == RESULT_OK) {
//                        File file = new File(AMCamera.mCurrentPhotoPath);
//                        Bitmap bitmap = MediaStore.Images.Media
//                                .getBitmap(getContentResolver(), Uri.fromFile(file));
//                        try {
//                            Log.d("SABE", "onActivityResult: ");
//                        this.nFotos = this.nFotos + 1;
//                        AMCamera.guardarFoto(bitmap, tarea.getIdpedido(), longitude, latitude);
//                        Log.d(Constants.TAG, "onActivityResult: " + this.nFotos);
//                    } catch (IOException e) {
//                        Log.e(Constants.TAG, "onActivityResult: ", e);
//                    }
//                    }
//                    break;
//                }
//            }
//
//        } catch (Exception error) {
//            error.printStackTrace();
//        }
/////
                try { // funcionando
        Log.d("YA", "onActivityResult: ");
        switch (requestCode) {
            case Constants.CAPTURE_IMAGE_REQUEST: {
                Log.d("YA0", "onActivityResult: ");
                if (resultCode == RESULT_OK) {
                    Log.d("YA1", AMCamera.mCurrentPhotoPath);
                    File file = new File(AMCamera.mCurrentPhotoPath);
                    Bitmap bitmap = MediaStore.Images.Media
                            .getBitmap(getContentResolver(), Uri.fromFile(file));
                    Log.d("BITMAP", String.valueOf(bitmap.getHeight()));
                    Log.d("BITMAP", String.valueOf(bitmap.getWidth()));
                    Log.d("BITMAP", String.valueOf(bitmap.getConfig()));
                    try {
                        Log.d("SABE", "onActivityResult: ");
                        this.nFotos = this.nFotos + 1;
                        AMCamera.guardarFoto(bitmap, tarea.getIdpedido(), longitude, latitude);
                        Log.d(Constants.TAG, "onActivityResult: " + this.nFotos);
                    } catch (IOException e) {
                        Log.e(Constants.TAG, "onActivityResult: ", e);
                    }
                }
                break;
            }
        }

    } catch (Exception error) {
        error.printStackTrace();
    }

//        if (resultCode == RESULT_OK) {
//            //pruebas foto
//            Log.d("YA", "onActivityResult: ");
//            switch (requestCode) {
//                case Constants.CAPTURE_IMAGE_REQUEST:
//                    File file = new File(AMCamera.mCurrentPhotoPath);
////                        Bitmap bitmap = MediaStore.Images.Media
////                                .getBitmap(getContentResolver(), Uri.fromFile(file));
////                        try {
////                            Log.d("SABE", "onActivityResult: ");
////                        this.nFotos = this.nFotos + 1;
////                        AMCamera.guardarFoto(bitmap, tarea.getIdpedido(), longitude, latitude);
////                        Log.d(Constants.TAG, "onActivityResult: " + this.nFotos);
//
//                    Bundle extras = data.getExtras();
//                    Bitmap imageBitmap = (Bitmap) extras.get("data");
////                    imageBitmap.
//                    try {
//                        this.nFotos = this.nFotos + 1;
//                        AMCamera.guardarFoto(imageBitmap, tarea.getIdpedido(), longitude, latitude);
//                        Log.d(Constants.TAG, "onActivityResult: " + this.nFotos);
//                    } catch (IOException e) {
//                        Log.e(Constants.TAG, "onActivityResult: ", e);
//                    }
//            }
//        }
        //////// original
//        if (resultCode == RESULT_OK) {
//            //pruebas foto
//            Log.d("YA", "onActivityResult: ");
//            switch (requestCode) {
//                case Constants.CAPTURE_IMAGE_REQUEST:
//
//
//                    Bundle extras = data.getExtras();
//                    Bitmap imageBitmap = (Bitmap) extras.get("data");
//                    Log.d("BITMAP", String.valueOf(imageBitmap.getHeight()));
//                    Log.d("BITMAP", String.valueOf(imageBitmap.getWidth()));
//                    Log.d("BITMAP", String.valueOf(imageBitmap.getConfig()));
////                    imageBitmap.
//                    try {
//                        this.nFotos = this.nFotos + 1;
//                        AMCamera.guardarFoto(imageBitmap, tarea.getIdpedido(), longitude, latitude);
//                        Log.d(Constants.TAG, "onActivityResult: " + this.nFotos);
//                    } catch (IOException e) {
//                        Log.e(Constants.TAG, "onActivityResult: ", e);
//                    }
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            //pruebas foto
            case Constants.CAPTURE_IMAGE_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, " Permiso otorgado, vuelva a abrir la cámara", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, " Permiso rechazado", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


}
