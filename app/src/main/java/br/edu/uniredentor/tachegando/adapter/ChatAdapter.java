package br.edu.uniredentor.tachegando.adapter;

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
import br.edu.uniredentor.tachegando.model.MensagemChat;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<MensagemChat> mensagens;

    public ChatAdapter(List<MensagemChat> mensagemChats) {
        mensagens = mensagemChats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MensagemChat mensagemChat = mensagens.get(position);
        holder.set(mensagemChat);
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    public void atualiza(ArrayList<MensagemChat> mensagens) {
        this.mensagens = mensagens;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewFoto;
        private TextView textViewNome;
        private TextView textViewData;
        private TextView textViewMensagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFoto = itemView.findViewById(R.id.imageView_foto_chat);
            textViewData = itemView.findViewById(R.id.textView_data_chat);
            textViewMensagem = itemView.findViewById(R.id.textView_mensagem);
            textViewNome = itemView.findViewById(R.id.textView_nome_chat);
        }

        public void set(MensagemChat mensagemChat) {
            GeralUtils.mostraImagemCircular(itemView.getContext(), imageViewFoto, mensagemChat.getFotoUsuario());
            textViewData.setText(mensagemChat.getDiaEHora());
            textViewNome.setText(mensagemChat.getNomeUsuario());
            textViewMensagem.setText(mensagemChat.getTexto());
        }
    }
}
