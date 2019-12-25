package br.edu.uniredentor.tachegando.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        Passageiro p = new Passageiro(
                );
        Passageiro p2 = new Passageiro();
        final MensagemChat mensagemChat = new MensagemChat();
        mensagemChat.setNomeUsuario("Arrascaeta");
        mensagemChat.setFotoUsuario("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg");
        mensagemChat.setTexto("Ganhou o brasileiro?");
        MensagemChat mensagemChat2 = new MensagemChat();
        mensagemChat2.setNomeUsuario("Everton");
        mensagemChat2.setFotoUsuario("https://colunadofla.com/wp-content/uploads/2019/09/everton-ribeiro-4.jpg");
        mensagemChat2.setTexto("Sim! E você, ganhou a libertadores?");

        RecyclerView recyclerViewChat = view.findViewById(R.id.recyclerView_chat);

        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        final ChatAdapter adapter = new ChatAdapter(Arrays.asList(mensagemChat, mensagemChat2));
        recyclerViewChat.setAdapter(adapter);
        recyclerViewChat.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        FirebaseUtils.getBanco().collection("chats").document("1").collection("conversas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                ArrayList<MensagemChat> mensagens = new ArrayList<>();
                mensagens.addAll(queryDocumentSnapshots.toObjects(MensagemChat.class));
                //adapter.atualiza(mensagens);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView imageViewEnvia = getView().findViewById(R.id.imageView_envia);
        final TextInputEditText editTextMensagem = getView().findViewById(R.id.editText_mensagem);
        imageViewEnvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Passageiro p = new Passageiro("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg",
                        "20 minutos", "Raphael");
                String mensagem = editTextMensagem.getText().toString();
                MensagemChat mensagemChat = new MensagemChat(p, mensagem, Calendar.getInstance());
                mensagemChat.setIdViagem("1");
                mensagemChat.setIdUsuario("123");
                FirebaseUtils.salvaMensagem(mensagemChat);
                editTextMensagem.setText("");
                */

            }
        });


    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }
}
