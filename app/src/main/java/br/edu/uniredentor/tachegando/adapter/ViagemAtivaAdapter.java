package br.edu.uniredentor.tachegando.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViagemAtivaAdapter extends RecyclerView.Adapter<ViagemAtivaAdapter.ViewHolder> {


    private List<Viagem> viagensAtivas;
    private Context context;

    public ViagemAtivaAdapter(List<Viagem> viagensAtivas, Context context) {
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
