package com.academiamoviles.distrack.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.adapters.EstadoTareaAdapter;
import com.academiamoviles.distrack.models.Estado;
import com.academiamoviles.distrack.models.User;
import com.academiamoviles.distrack.services.AMApiService;
import com.academiamoviles.distrack.services.AMApiServiceGenerator;
import com.academiamoviles.distrack.services.AMLocationManager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class EstadoTareaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_tarea);
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

        List<Estado> estadosTotal = Realm.getDefaultInstance().where(Estado.class).findAll();
        List<Estado> estados = new ArrayList<>();
        for (Estado estado : estadosTotal) {
            if (estado.getMotivos().size() > 0) {
                if (estado.getMotivos().get(0).getFinaliza().equals("1")) {
                    estados.add(estado);
                }
            }
        }

        EstadoTareaAdapter estadoTareaAdapter = new EstadoTareaAdapter(estados, this, this, data.getString("comentario"));

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.listaEstados);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(estadoTareaAdapter);
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
