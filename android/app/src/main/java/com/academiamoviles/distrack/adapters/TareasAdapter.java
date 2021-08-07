package com.academiamoviles.distrack.adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;

import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.academiamoviles.distrack.activities.ActualizarPendienteActivity;
import com.academiamoviles.distrack.activities.DetalleTareaActivity;
import com.academiamoviles.distrack.activities.EditarPendienteActivity;
import com.academiamoviles.distrack.models.RegEstado;
import com.academiamoviles.distrack.models.ResponseRegEstado;
import com.academiamoviles.distrack.models.Tarea;
import com.academiamoviles.distrack.services.AMLocationManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.ViewHolder> {

    private List<Tarea> tareas;
    private Fragment fragment;
    private boolean sincronizar;

    public TareasAdapter(boolean sincronizar) {
        this.sincronizar = sincronizar;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tareaCliente;
        TextView tareaDocumento;
        TextView tareaVolumen;
        TextView tareaPeso;
        TextView tareaDircliente;
        TextView tareaPlanillla;

        ViewHolder(View itemView) {
            super(itemView);
            tareaCliente = (TextView) itemView.findViewById(R.id.tareaCliente);
            tareaDocumento = (TextView) itemView.findViewById(R.id.tareaDocumento);
            tareaVolumen = (TextView) itemView.findViewById(R.id.tareaVolumen);
            tareaPeso = (TextView) itemView.findViewById(R.id.tareaPeso);
            tareaDircliente = (TextView) itemView.findViewById(R.id.tareaDircliente);
            tareaPlanillla = (TextView) itemView.findViewById(R.id.tareaPlanillla);
        }
    }

    @Override
    public TareasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TareasAdapter.ViewHolder viewHolder, final int position) {
        final Tarea tarea = this.tareas.get(position);
        viewHolder.tareaCliente.setText(tarea.getCliente());
        viewHolder.tareaDocumento.setText(tarea.getDocumento());
        viewHolder.tareaVolumen.setText(tarea.getCantidad());
        viewHolder.tareaPeso.setText(tarea.getPeso());
        viewHolder.tareaDircliente.setText(tarea.getDircliente());
        viewHolder.tareaPlanillla.setText(tarea.getAux5());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sincronizar) {
                    Intent intent = new Intent(fragment.getContext(), DetalleTareaActivity.class);
                    Constants.tarea = tarea;
                    fragment.getActivity().startActivity(intent);
                } else if (tarea.getIniciado2()) {
                    Intent intent = new Intent(fragment.getContext(), EditarPendienteActivity.class);
                    Constants.tarea = tarea;
                    intent.putExtra("detalle", true);
                    fragment.getActivity().startActivity(intent);
                } else if (tarea.getIniciado1()) {
                    Intent intent = new Intent(fragment.getContext(), EditarPendienteActivity.class);
                    intent.putExtra("detalle", false);
                    Constants.tarea = tarea;
                    fragment.getActivity().startActivity(intent);
                } else if (tarea.getLlegada()) {
                    Intent intent = new Intent(fragment.getContext(), ActualizarPendienteActivity.class);
                    Constants.tarea = tarea;
                    fragment.getActivity().startActivity(intent);
                } else {
                    alertaTarea(fragment, tarea).show();
                }
            }
        });
    }


    private AlertDialog alertaTarea(final Fragment fragment, final Tarea tarea) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        builder.setTitle(Constants.MENSAJE_TAREACONFIRMACION);
        builder.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(fragment.getContext(), ActualizarPendienteActivity.class);
                Constants.tarea = tarea;
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Tarea.class)
                                .equalTo("idpedido", tarea.getIdpedido())
                                .equalTo("documento", tarea.getDocumento())
                                .findFirst()
                                .setLlegada(true);
                    }
                });
                guardarEstado(tarea.getDocumento());
                dialog.dismiss();
                fragment.getActivity().startActivity(intent);

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(fragment.getContext(), DetalleTareaActivity.class);
                Constants.tarea = tarea;
                fragment.getActivity().startActivity(intent);
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public int getItemCount() {
        return this.tareas.size();
    }

    private void guardarEstado(String documento) {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final RegEstado regEstado = new RegEstado();
        regEstado.setDocumento(documento);
        regEstado.setUsuario(Constants.USUARIO.getUsuario());
        regEstado.setUsuario_id(Constants.USUARIO.getId());
        regEstado.setEstado("Llegada");
        regEstado.setMotivo("Llegada");
        regEstado.setCobranza("");
        regEstado.setFecha(dateFormat.format(currentTime));
        regEstado.setLatitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLatitude()));
        regEstado.setLongitud(String.valueOf(AMLocationManager.getInstance().getLastLocation().getLongitude()));
        regEstado.setComentario("");
        regEstado.setCod_estado("EST0000");
        regEstado.setCod_motivo("EST0000");
        regEstado.setCod_cobranza("");
        List<RegEstado> regEstadoList = new ArrayList<>();
        regEstadoList.add(regEstado);
        HashMap<String, List<RegEstado>> map = new HashMap<>();
        map.put("data", regEstadoList);
        Constants.APISEVICE.sendRegEstado(map).enqueue(new Callback<List<ResponseRegEstado>>() {
            @Override
            public void onResponse(Call<List<ResponseRegEstado>> call, Response<List<ResponseRegEstado>> response) {
                Log.d(Constants.TAG, "onResponseGuardarEstado: " + response.body().get(0).getSuccess());
            }

            @Override
            public void onFailure(Call<List<ResponseRegEstado>> call, Throwable t) {
                Log.d(Constants.TAG, "onFailure: " + t.toString());
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(regEstado);
                    }
                });
            }
        });
    }
}
