package br.edu.uniredentor.tachegando.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class PassageiroAdapter extends RecyclerView.Adapter<PassageiroAdapter.ViewHolder>{

    private List<Passageiro> passageiros = new ArrayList<>();

    public PassageiroAdapter(List<Passageiro> passageiros) {
        this.passageiros = passageiros;
    }

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

        private ImageView imageViewFoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFoto = itemView.findViewById(R.id.imageView_foto);
        }

        public void set(Passageiro passageiro) {
            GeralUtils.mostraImagemCircular(itemView.getContext(), imageViewFoto, passageiro.getFoto());
        }
    }
}
