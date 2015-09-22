package com.example.sebastian.tt_rapid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class Adapter_RecyclerView_Table extends RecyclerView.Adapter<Adapter_RecyclerView_Table.MyViewHolder> {

    private LayoutInflater inflater;
    List<Table_Data> data = Collections.emptyList();
    public Context context_adapter;
    private String _mannschaft;


    public void setMannschaft(String mannschaft) {
        _mannschaft = mannschaft;
    }

    public Adapter_RecyclerView_Table(Context context, List<Table_Data> data) {
        inflater = LayoutInflater.from(context);
        this.context_adapter=context;
        this.data=data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_table, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Table_Data current = data.get(position);

        if (current.mannschaft.equals(_mannschaft)){

            holder.platz.setTextColor(context_adapter.getResources().getColor(R.color.colorPrimary));
            holder.mannschaft.setTextColor(context_adapter.getResources().getColor(R.color.colorPrimary));
            holder.spielteilnahmen.setTextColor(context_adapter.getResources().getColor(R.color.colorPrimary));
            holder.spiele.setTextColor(context_adapter.getResources().getColor(R.color.colorPrimary));
            holder.punkte.setTextColor(context_adapter.getResources().getColor(R.color.colorPrimary));
        }

        holder.platz.setText(current.platz);
        holder.mannschaft.setText(current.mannschaft);
        holder.spielteilnahmen.setText(current.spielteilnahmen);
        holder.spiele.setText(current.spiele);
        holder.punkte.setText(current.punkte);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView platz,mannschaft,spielteilnahmen,spiele,punkte;


        public MyViewHolder(View itemView) {
            super(itemView);


            platz= (TextView) itemView.findViewById(R.id.Platz);
            mannschaft= (TextView) itemView.findViewById(R.id.Mannschaft);
            spielteilnahmen= (TextView) itemView.findViewById(R.id.Spielteilnahmen);
            spiele= (TextView) itemView.findViewById(R.id.Spiele);
            punkte= (TextView) itemView.findViewById(R.id.Punkte);

        }
    }
}
