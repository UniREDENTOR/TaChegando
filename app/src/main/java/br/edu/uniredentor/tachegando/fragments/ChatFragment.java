package br.edu.uniredentor.tachegando.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.ChatAdapter;
import br.edu.uniredentor.tachegando.model.MensagemChat;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private Viagem viagem;
    private ChatAdapter adapter;
    private List<MensagemChat> mensagens;
    private FirebaseUser user;

    @BindView(R.id.recyclerView_chat) RecyclerView recyclerViewChat;
    @BindView(R.id.editText_mensagem) EditText editTextMensagem;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        mensagens = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewChat.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(mensagens);
        recyclerViewChat.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        recyclerViewChat.setAdapter(adapter);
        recyclerViewChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerViewChat.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewChat.scrollToPosition(mensagens.size());
                        }
                    });
                }
            }
        });

        user = FirebaseUtils.getUser();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUtils.getConversas(viagem.getId()).orderBy("dataCriacao", Query.Direction.DESCENDING).limit(20)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
            mensagens.clear();
            for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                if (document.exists()) {
                    mensagens.add(document.toObject(MensagemChat.class));
                    adapter.notifyDataSetChanged();
                }
            }
            if(mensagens.size() > 0) {
                adapter.atualiza(mensagens);
            }
        });
    }

    @OnClick(R.id.fab_enviar)
    public void enviarMensagem(){
        String mensagem = editTextMensagem.getText().toString();

        try{
            if(GeralUtils.ehUsuario(getActivity())){
                MensagemChat mensagemChat = new MensagemChat();
                mensagemChat.setNomeUsuario(user.getDisplayName());
                mensagemChat.setFotoUsuario(user.getPhotoUrl().toString());
                mensagemChat.setIdUsuario(user.getUid());
                mensagemChat.setTexto(mensagem);
                mensagemChat.setDataCriacao(Calendar.getInstance().getTimeInMillis());
                editTextMensagem.setText("");
                FirebaseUtils.getConversas(viagem.getId()).add(mensagemChat.getMap());
            }

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }
}
