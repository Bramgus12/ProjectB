package com.bramgussekloo.projectb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bramgussekloo.projectb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReservationsBeheerderFragment extends Fragment {


    public ReservationsBeheerderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations_beheerder, container, false);
    }

}
