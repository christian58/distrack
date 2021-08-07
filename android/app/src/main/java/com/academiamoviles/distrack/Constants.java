package com.academiamoviles.distrack;

import android.Manifest;
import android.os.Environment;

import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;

import java.io.File;

import io.realm.Realm;

/**
 * Created by maacs on 15/09/2017.
 */

public class Constants {
    //TAG
    public static final String TAG = "Tracklog";

    //request
    public static final int UBICACION_REQUEST = 400;
    public static final int CAPTURE_IMAGE_REQUEST = 300;

    //permisos gps
    public static final String[] PERMISSIONS_LIST_GPS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //permisos camara
    public static final String[] PERMISSIONS_LIST_CAMERA = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //directorio foto
    public static final String FOTO_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "DistrackFotos";
    //archivo pdf-ayuda
    public static final String PDF_DIRECTORY = "test.pdf";

    //mensaje alerta
    public static final String MENSAJE_NOLOCATION = "Sincronizando posición actual, espere unos momentos . . .";
    public static final String MENSAJE_TAREACONFIRMACION = "¿Desea registrar su llegada?";
    public static final String MENSAJE_SALIDACONFIRMACION = "¿Seguro que desea salir del app?";
    public static final String MENSAJE_SALIDACONFIRMACIONSININTERNET = "¿Se perderá la data no sincronizada, seguro que desea salir?";

    public static final String BASE_URL = "http://tracklogservice.com:3010/";

    public static User USUARIO;
    public static AMApiService APISEVICE;
    public static Realm REALM;
    public static Tarea tarea;

}
