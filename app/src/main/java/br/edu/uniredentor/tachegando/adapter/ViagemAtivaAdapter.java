package br.edu.uniredentor.tachegando.adapter;

import android.graphics.drawable.Icon;
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

public class ViagemAtivaAdapter extends RecyclerView.Adapter<ViagemAtivaAdapter.ViewHolder> {


    private List<Viagem> viagensAtivas = new ArrayList<>();

    public ViagemAtivaAdapter(List<Viagem> viagensAtivas) {
        this.viagensAtivas = viagensAtivas;
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
        holder.set(viagem);

    }

    @Override
    public int getItemCount() {
        return viagensAtivas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageCriadorViagem;
        private TextView nomeViagemAtiva;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCriadorViagem = itemView.findViewById(R.id.imageView_criador_lista_viagem_ativa);
            nomeViagemAtiva = itemView.findViewById(R.id.text_view_nome_viagem_ativa);

        }

        public void set(Viagem viagem) {
            Passageiro passageiro = new Passageiro();
            GeralUtils.mostraImagemCircular(itemView.getContext(), imageCriadorViagem, passageiro.getFoto());
            nomeViagemAtiva.setText(viagem.getNome());
        }
    }
}
