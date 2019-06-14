package com.bramgussekloo.projectb.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bramgussekloo.projectb.Activities.ReturnActivity;
import com.bramgussekloo.projectb.Activities.sendNotification;
import com.bramgussekloo.projectb.Adapter.AdminReservationsRecyclerAdapter;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;
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
public class ReservationsAdminFragment extends Fragment implements AdminReservationsRecyclerAdapter.OnReturnListener {

    private static final String TAG = "HomeFragment";
    private RecyclerView lend_items_list_view;
    private List<Lend> lend_list;
    private FirebaseFirestore firebaseFirestore;
    private AdminReservationsRecyclerAdapter adminRecyclerAdapter;


    public ReservationsAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lend_items_admin, container, false);

        lend_list = new ArrayList<>();
        lend_items_list_view = view.findViewById(R.id.lend_items_list_view_admin);
        firebaseFirestore = FirebaseFirestore.getInstance();
        adminRecyclerAdapter = new AdminReservationsRecyclerAdapter(lend_list, this);

        lend_items_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));

        lend_items_list_view.setAdapter(adminRecyclerAdapter);

        firebaseFirestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String productId = doc.getDocument().getId();

                        firebaseFirestore.collection("Products/" + productId + "/LendTo").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        String nameId = doc.getDocument().getId();

                                        Lend lend = doc.getDocument().toObject(Lend.class).withId(nameId);
                                        lend_list.add(lend);

                                        adminRecyclerAdapter.notifyDataSetChanged();


                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onRturnClick(int posistion) {
        Intent intent = new Intent(getContext(), ReturnActivity.class);
        intent.putExtra("item", lend_list.get(posistion));
        startActivity(intent);
    }
}
