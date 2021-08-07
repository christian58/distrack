package com.academiamoviles.distrack.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;
import com.academiamoviles.distrack.services.AMLocationManager;

import io.realm.Realm;

import static com.academiamoviles.distrack.Constants.tarea;

public class DetalleTareaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Constants.REALM == null) {
            Realm.init(this);
            Constants.REALM = Realm.getDefaultInstance();
            Constants.USUARIO = Constants.REALM.where(User.class).findFirst();
            Constants.APISEVICE = AMApiServiceGenerator.createService(AMApiService.class);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        AMLocationManager.getInstance().setActivity(this);

        ((TextView) findViewById(R.id.tareaCliente)).setText(tarea.getCliente());
        ((TextView) findViewById(R.id.tareaDocumento)).setText(tarea.getDocumento());
        ((TextView) findViewById(R.id.tareaVolumen)).setText(tarea.getCantidad());
        ((TextView) findViewById(R.id.tareaPeso)).setText(tarea.getPeso());
        ((TextView) findViewById(R.id.tareaDircliente)).setText(tarea.getDircliente());
        ((TextView) findViewById(R.id.tareaPlanillla)).setText(tarea.getAux5());

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

}
