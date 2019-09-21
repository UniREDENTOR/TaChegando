package br.edu.uniredentor.tachegando.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class PassageiroAdapter extends RecyclerView.Adapter<PassageiroAdapter.ViewHolder>{

    private ArrayList<Passageiro> passageiros;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_passageiro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Passageiro passageiro = passageiros.get(position);
        holder.set(passageiro);
    }

    @Override
    public int getItemCount() {
        return passageiros.size();
    }

    public void atualiza(ArrayList<Passageiro> passageiros){
        this.passageiros = passageiros;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewTempo;
        private ImageView imageViewFoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTempo = itemView.findViewById(R.id.textView_tempo);
            imageViewFoto = itemView.findViewById(R.id.imageView_foto);
        }

        public void set(Passageiro passageiro) {
            textViewTempo.setText(passageiro.getTempo());
            GeralUtils.mostraImagemCircular(itemView.getContext(), imageViewFoto, passageiro.getFoto());
        }
    }
}
