package br.edu.uniredentor.tachegando.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.activity.ViagensAtivasActivity;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.ConstantsUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import br.edu.uniredentor.tachegando.utils.MapaUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViagemAtivaAdapter extends RecyclerView.Adapter<ViagemAtivaAdapter.ViewHolder> {


    private List<Viagem> viagensAtivas;
    private Activity context;

    public ViagemAtivaAdapter(List<Viagem> viagensAtivas, Activity context) {
        this.viagensAtivas = viagensAtivas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_viagem_ativa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Viagem viagem = viagensAtivas.get(position);

        holder.nomeCriadorViagem.setText(viagem.getPassageiros().get(0).getNome());
        holder.nomeViagemAtiva.setText(viagem.getNome());
        GeralUtils.mostraImagemCircular(context,holder.imageCriadorViagem,viagem.getPassageiros().get(0).getFoto());
        holder.itemView.setOnClickListener(v -> {

            Intent it = new Intent();
            it.putExtra(ConstantsUtils.VIAGEM, viagem);
            context.setResult(Activity.RESULT_OK, it);
            context.finish();
        });
    }

    @Override
    public int getItemCount() {
        if(viagensAtivas == null) {
            return 0;
        }
        return viagensAtivas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView_criador_lista_viagem_ativa) ImageView imageCriadorViagem;
        @BindView(R.id.text_view_nome_viagem_ativa) TextView nomeViagemAtiva;
        @BindView(R.id.text_view_nome_criador_viagem) TextView nomeCriadorViagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
