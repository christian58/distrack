package com.academiamoviles.distrack.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.adapters.TareasAdapter;
import com.academiamoviles.distrack.models.Tarea;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SincronizarFragment extends Fragment {

    private EditText cajaBuscar;
    private TareasAdapter adapter;
    private int res = 0;

    public SincronizarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sincronizar, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTareas);
        TextView botonBuscar = (TextView) view.findViewById(R.id.botonBuscar);
        cajaBuscar = (EditText) view.findViewById(R.id.cajaBuscar);

        adapter = new TareasAdapter(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Tarea> tareasList = realm.where(Tarea.class).equalTo("sincronizar", true).findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING);
        adapter.setFragment(this);
        adapter.setTareas(tareasList);
        adapter.notifyDataSetChanged();

        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cajaBuscar.getText().toString().equals("")) {
                    adapter.setTareas(Realm.getDefaultInstance()
                            .where(Tarea.class)
                            .equalTo("sincronizar", true)
                            .findAll());
                } else {
                    res = Realm.getDefaultInstance().where(Tarea.class).equalTo("idpedido", cajaBuscar.getText().toString()).findAll().size();
                    if (res == 0) {
                        adapter.setTareas(Realm.getDefaultInstance()
                                .where(Tarea.class)
                                .equalTo("sincronizar", true)
                                .equalTo("aux5", cajaBuscar.getText().toString())
                                .findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING));
                    } else {
                        adapter.setTareas(Realm.getDefaultInstance()
                                .where(Tarea.class)
                                .equalTo("sincronizar", true)
                                .equalTo("idpedido", cajaBuscar.getText().toString())
                                .findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

}
