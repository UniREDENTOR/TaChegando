package br.edu.uniredentor.tachegando.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PassageiroAdapter extends RecyclerView.Adapter<PassageiroAdapter.ViewHolder>{

    private List<Passageiro> passageiros;

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

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imageView_foto) ImageView imageViewFoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void set(Passageiro passageiro) {
            GeralUtils.mostraImagemCircular(itemView.getContext(), imageViewFoto, passageiro.getFoto());
        }
    }
}
