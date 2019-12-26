package br.edu.uniredentor.tachegando.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import br.edu.uniredentor.tachegando.R;
import br.edu.uniredentor.tachegando.adapter.ChatAdapter;
import br.edu.uniredentor.tachegando.model.MensagemChat;
import br.edu.uniredentor.tachegando.model.Passageiro;
import br.edu.uniredentor.tachegando.model.Viagem;
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private Viagem viagem;
    private ChatAdapter adapter;
    private List<MensagemChat> mensagens;
    private RecyclerView recyclerViewChat;

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mensagens = new ArrayList<>();

        recyclerViewChat = view.findViewById(R.id.recyclerView_chat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewChat.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(mensagens);
        recyclerViewChat.setAdapter(adapter);
        recyclerViewChat.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView imageViewEnvia = getView().findViewById(R.id.imageView_envia);
        final TextInputEditText editTextMensagem = getView().findViewById(R.id.editText_mensagem);
        FirebaseUtils.getConversas("").orderBy("dataCriacao", Query.Direction.DESCENDING).limit(20).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mensagens.clear();
                for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    if (document.exists()) {
                        mensagens.add(document.toObject(MensagemChat.class));
                    }
                }
                if(mensagens.size() > 0) {
                    adapter.atualiza(mensagens);
                }
            }
        });

        imageViewEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensagem = editTextMensagem.getText().toString();
                final MensagemChat mensagemChat = new MensagemChat();
                mensagemChat.setNomeUsuario("Gabigol");
                mensagemChat.setFotoUsuario("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg");
                mensagemChat.setIdUsuario("12345");
                mensagemChat.setTexto(mensagem);
                editTextMensagem.setText("");
                FirebaseUtils.getConversas(viagem.getId()).add(mensagemChat.getMap());
            }
        });
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }
}
