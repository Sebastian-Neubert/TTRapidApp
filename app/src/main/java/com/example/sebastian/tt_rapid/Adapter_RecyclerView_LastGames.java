package com.example.sebastian.tt_rapid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Sebastian on 09.09.2015.
 */
public class Adapter_RecyclerView_LastGames extends RecyclerView.Adapter<Adapter_RecyclerView_LastGames.MyViewHolder> {

    private LayoutInflater inflater;
    List<LastGames_SpielDaten> data = Collections.emptyList();
    public Context context_adapter;
    private String _mannschaft;


    public void setMannschaft(String mannschaft) {
        _mannschaft = mannschaft;
    }

    public Adapter_RecyclerView_LastGames(Context context, List<LastGames_SpielDaten> data) {
        inflater = LayoutInflater.from(context);
        this.context_adapter=context;
        this.data=data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_spiele, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LastGames_SpielDaten current = data.get(position);

        if (current.heim.equals(_mannschaft)){
            holder.heim.setTextColor(context_adapter.getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }

        if (current.gast.equals(_mannschaft)){
            holder.gast.setTextColor(context_adapter.getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }

        holder.heim.setText(current.heim);
        holder.gast.setText(current.gast);
        holder.erg.setText(current.erg);
        holder.datum.setText(current.datum);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView heim,gast,erg,datum;


        public MyViewHolder(View itemView) {
            super(itemView);

            heim = (TextView) itemView.findViewById(R.id.Heimmannschaft);
            gast = (TextView) itemView.findViewById(R.id.Gastmannschaft);
            erg = (TextView) itemView.findViewById(R.id.Ergebnis);
            datum = (TextView) itemView.findViewById(R.id.Datum);

        }
    }
}
