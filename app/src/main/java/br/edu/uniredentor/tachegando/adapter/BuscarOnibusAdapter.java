package br.edu.uniredentor.tachegando.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.fragments.BuscarOnibusDialogFragment;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;
import butterknife.BindView;
import butterknife.ButterKnife;


public class BuscarOnibusAdapter extends RecyclerView.Adapter<BuscarOnibusAdapter.ViewHolder> {

    private List<Viagem> listaDeViagensFiltrada;
    private Context context;
    private GoogleMap mapa;
    private BuscarOnibusDialogFragment buscarOnibusDialogFragment;

    public BuscarOnibusAdapter(List<Viagem> listaDeViagensFiltrada, Context context, GoogleMap mapa, BuscarOnibusDialogFragment buscarOnibusDialogFragment) {
        this.listaDeViagensFiltrada = listaDeViagensFiltrada;
        this.context = context;
        this.mapa = mapa;
        this.buscarOnibusDialogFragment = buscarOnibusDialogFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_buscar_onibus, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Viagem viagem = listaDeViagensFiltrada.get(position);
        Passageiro passageiro = viagem.getCriador(viagem);

        holder.textViewRotaOnibus.setText(viagem.getNome().toLowerCase());
        holder.textViewCriadorRota.setText(passageiro.getNome());
        GeralUtils.mostraImagemCircular(context,holder.imageViewCriadorRota,passageiro.getFoto());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(viagem.getLatitude(), viagem.getLongitude());
                MapaUtils.moveCamera(mapa, latLng);
                buscarOnibusDialogFragment.dismiss();
                Log.d("teste", viagem.getLatitude() + viagem.getLongitude() + "");
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaDeViagensFiltrada.size();
    }

    public void atualiza(List<Viagem> listaDeViagensFiltrada) {
        this.listaDeViagensFiltrada = listaDeViagensFiltrada;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_rota_onibus) TextView textViewRotaOnibus;
        @BindView(R.id.textView_criador_rota) TextView textViewCriadorRota;
        @BindView(R.id.imageView_criador_rota) ImageView imageViewCriadorRota;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

