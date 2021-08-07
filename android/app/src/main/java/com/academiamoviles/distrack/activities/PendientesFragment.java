package com.academiamoviles.distrack.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.adapters.TareasAdapter;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.services.AMScannerManager;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class PendientesFragment extends Fragment {

    private EditText cajaBuscar;
    private TareasAdapter adapter;
    private int res = 0;
    private RealmResults<Tarea> tareasList;

    public PendientesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendientes, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listaTareas);
        TextView botonBuscar = (TextView) view.findViewById(R.id.botonBuscar);
        FloatingActionButton scanButton = (FloatingActionButton) view.findViewById(R.id.fab);
        cajaBuscar = (EditText) view.findViewById(R.id.cajaBuscar);

        adapter = new TareasAdapter(false);
        final Realm realm = Realm.getDefaultInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AMScannerManager.class);
                getContext().startActivity(intent);
            }
        });
        tareasList = realm.where(Tarea.class).equalTo("sincronizar", false).findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING);
        adapter.setFragment(this);
        adapter.setTareas(tareasList);
        adapter.notifyDataSetChanged();
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cajaBuscar.getText().toString().equals("")) {
                    adapter.setTareas(tareasList);
                } else {
                    RealmResults<Tarea> tareas = tareasList.where()
                            .contains("cliente", cajaBuscar.getText().toString(), Case.INSENSITIVE)
                            .findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING);
                    res = tareas.size();
                    if (res == 0) {
                        tareas = tareasList.where()
                                .contains("documento", cajaBuscar.getText().toString(), Case.INSENSITIVE)
                                .findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING);
                        res = tareas.size();
                        if (res == 0) {
                            tareas = tareasList.where()
                                    .contains("aux5", cajaBuscar.getText().toString(), Case.INSENSITIVE)
                                    .findAllSorted("cliente", Sort.ASCENDING, "documento", Sort.ASCENDING);
                            res = tareas.size();
                            adapter.setTareas(tareas);
                        } else {
                            adapter.setTareas(tareas);
                        }
                    } else {
                        adapter.setTareas(tareas);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}
