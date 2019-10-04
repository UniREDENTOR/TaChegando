package br.edu.uniredentor.tachegando.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import br.edu.uniredentor.tachegando.utils.FirebaseUtils;
import br.edu.uniredentor.tachegando.utils.GeralUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        Passageiro p = new Passageiro("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg",
                "20 minutos");
        Passageiro p2 = new Passageiro("https://colunadofla.com/wp-content/uploads/2019/09/everton-ribeiro-4.jpg", "25 minutos");
        final MensagemChat mensagemChat = new MensagemChat(p, "E aí, tudo bem?", Calendar.getInstance());
        MensagemChat mensagemChat2 = new MensagemChat(p2, "tranquilo e tu?", Calendar.getInstance());
        RecyclerView recyclerViewChat = view.findViewById(R.id.recyclerView_chat);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        final ChatAdapter adapter = new ChatAdapter(Arrays.asList(mensagemChat, mensagemChat2));
        recyclerViewChat.setAdapter(adapter);
        FirebaseUtils.getBanco().collection("chats").document("1").collection("conversas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                ArrayList<MensagemChat> mensagens = new ArrayList<>();
                mensagens.addAll(queryDocumentSnapshots.toObjects(MensagemChat.class));
                adapter.atualiza(mensagens);
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
                Passageiro p = new Passageiro("https://upload.wikimedia.org/wikipedia/commons/4/47/20171114_AUT_URU_4546_%28cropped%29.jpg",
                        "20 minutos", "Raphael");
                String mensagem = editTextMensagem.getText().toString();
                MensagemChat mensagemChat = new MensagemChat(p, mensagem, Calendar.getInstance());
                mensagemChat.setIdViagem("1");
                mensagemChat.setIdUsuario("123");
                FirebaseUtils.salvaMensagem(mensagemChat);
                editTextMensagem.setText("");
            }
        });


    }
}
