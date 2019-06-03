package com.bramgussekloo.projectb.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bramgussekloo.projectb.Activities.LendActivity;
import com.bramgussekloo.projectb.Adapter.AdminhistoryrecyclerAdapter;
import com.bramgussekloo.projectb.Activities.DeleteReservation;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Reservation;
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
public class HistoryAdminFragment extends Fragment implements AdminhistoryrecyclerAdapter.OnDeleteListener, AdminhistoryrecyclerAdapter.OnLendClick {
    private RecyclerView history_list_view_admin;
    private List<Reservation> reservationList;
    private FirebaseFirestore firebaseFirestore;
    private AdminhistoryrecyclerAdapter adminhistoryrecyclerAdapter;




    public HistoryAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_admin,container,false);
        reservationList = new ArrayList<>();
        adminhistoryrecyclerAdapter = new AdminhistoryrecyclerAdapter(reservationList,this, this);
        history_list_view_admin = view.findViewById(R.id.history_list_view_admin);
        history_list_view_admin.setLayoutManager(new LinearLayoutManager(getActivity()));
        history_list_view_admin.setAdapter(adminhistoryrecyclerAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String productId = doc.getDocument().getId();
                        firebaseFirestore.collection("Products/" + productId + "/reservation").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        String nameId = doc.getDocument().getId();

                                        Reservation reservation = doc.getDocument().toObject(Reservation.class).withId(nameId);
                                        reservationList.add(reservation);
                                        adminhistoryrecyclerAdapter.notifyDataSetChanged();


                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    public void onDeleteClick(int position) {
        Intent intent = new Intent(getContext(), DeleteReservation.class);
        intent.putExtra("item", reservationList.get(position));
        startActivity(intent);
    }

    @Override
    public void onLendClick(int position) {
        Intent intent = new Intent(getContext(), LendActivity.class);
        intent.putExtra("Item", reservationList.get(position));
        startActivity(intent);
    }


}
