package com.bramgussekloo.projectb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bramgussekloo.projectb.Adapter.historyRecyclerAdpter;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.bramgussekloo.projectb.models.history;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    private RecyclerView history_list_view;
    private List<history> history_list;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private historyRecyclerAdpter historyRecyclerAdpter;




    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);
        history_list = new ArrayList<>();
        history_list_view = view.findViewById(R.id.history_list_view);
        historyRecyclerAdpter = new historyRecyclerAdpter(history_list);
        history_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        history_list_view.setAdapter(historyRecyclerAdpter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getEmail().replace(".", "").replace("@", "");
        firebaseFirestore.collection("history").document(currentUserId).collection("userHistory").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        history historyItem = doc.getDocument().toObject(history.class);
                        history_list.add(historyItem);
                        historyRecyclerAdpter.notifyDataSetChanged();


                    }
                }
                }
        });
        // Inflate the layout for this fragment
        return view;
    }

}
