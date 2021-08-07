package com.academiamoviles.distrack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.academiamoviles.distrack.Constants;
import com.academiamoviles.distrack.R;
import com.academiamoviles.distrack.models.Foto;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import io.realm.RealmResults;

/**
 * Created by maacs on 04/10/2017.
 */

public class FotosTareaAdapter extends RecyclerView.Adapter<FotosTareaAdapter.ViewHolder> {
    private RealmResults<Foto> fotos;
    private Context context;

    public FotosTareaAdapter(Context context, RealmResults<Foto> fotos) {
        this.fotos = fotos;
        this.context = context;
    }

    public void setFotos(RealmResults<Foto> fotos) {
        this.fotos = fotos;
    }

    @Override
    public FotosTareaAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_foto, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FotosTareaAdapter.ViewHolder viewHolder, int i) {
        if (fotos.size() > i) {
            Picasso.with(context).load(new File(fotos.get(i).getUrl())).placeholder(R.drawable.cuadro_foto).into(viewHolder.foto);
        } else {
            Picasso.with(context).load(R.drawable.cuadro_foto).into(viewHolder.foto);
        }
    }

    @Override
    public int getItemCount() {
        return Constants.USUARIO.getMax_foto();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView foto;

        public ViewHolder(View view) {
            super(view);
            foto = (ImageView) view.findViewById(R.id.foto);
        }
    }
}
