package com.academiamoviles.distrack.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.activities.EditarPendienteActivity;
import com.academiamoviles.distrack.models.Estado;
import com.academiamoviles.distrack.models.Motivo;
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

import static com.academiamoviles.distrack.Constants.tarea;


public class EstadoTareaAdapter extends BaseExpandableListAdapter {

    private List<Estado> estadoList;
    private Context context;
    private Activity activity;
    private String comentario;

    public EstadoTareaAdapter(List<Estado> estadoList, Context context, Activity activity, String comentario) {
        this.estadoList = estadoList;
        this.context = context;
        this.activity = activity;
        this.comentario = comentario;
    }

    @Override
    public int getGroupCount() {
        return estadoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return estadoList.get(groupPosition).getMotivos().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return estadoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return estadoList.get(groupPosition).getMotivos().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final Estado estado = estadoList.get(groupPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_estado, parent, false);
        convertView.getLayoutParams().height = parent.getMeasuredHeight() / 4;
        convertView.setPadding(0, 0, 0, 0);
        TextView titulo = (TextView) convertView.findViewById(R.id.tituloEstado);
        titulo.setText(estado.getEstado());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_motivo, parent, false);
        final Estado estado = estadoList.get(groupPosition);
        final Motivo motivo = estado.getMotivos().get(childPosition);
        RadioButton titulo = (RadioButton) convertView.findViewById(R.id.tituloSubEstado);
        titulo.setText(motivo.getLabel());
        convertView.setPadding(0, 0, 0, 0);
        titulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditarPendienteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("detalle", true);
                intent.putExtra("comentario", comentario);
                Constants.REALM.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        tarea.setTareaEstado(estado.getEstado());
                        tarea.setTareaCodEstado(motivo.getCodigo());
                        tarea.setTareaMotivo(motivo.getLabel());
                        tarea.setTareaCodMotivo(motivo.getCodigo());
                        tarea.setComentario(comentario);
                        tarea.setIniciado2(true);
                    }
                });
                activity.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
