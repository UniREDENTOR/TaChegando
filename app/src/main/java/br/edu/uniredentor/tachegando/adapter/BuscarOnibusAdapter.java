package br.edu.uniredentor.tachegando.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Viagem;



public class BuscarOnibusAdapter extends RecyclerView.Adapter<BuscarOnibusAdapter.ViewHolder> {

    private List<Viagem> listaDeViagensFiltrada;
    private Context context;



    public BuscarOnibusAdapter(List<Viagem> listaDeViagensFiltrada, Context context) {
        this.listaDeViagensFiltrada = listaDeViagensFiltrada;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_buscar_onibus, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Viagem viagem = listaDeViagensFiltrada.get(position);

        holder.textViewRotaOnibus.setText(viagem.getNome());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //mover camera pro onibus escolhido
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaDeViagensFiltrada.size();
    }

    public void atualiza(List<Viagem> listaDeViagensFiltrada){
        this.listaDeViagensFiltrada = listaDeViagensFiltrada;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewRotaOnibus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewRotaOnibus = itemView.findViewById(R.id.textView_rota_onibus);

        }
    }
}
