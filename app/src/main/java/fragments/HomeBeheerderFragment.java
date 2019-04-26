package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bramgussekloo.projectb.Adapter.AdminRecyclerAdapter;
import com.bramgussekloo.projectb.Adapter.ProductRecyclerAdapter;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
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
public class HomeBeheerderFragment extends Fragment {

    private RecyclerView product_list_view;
    private List<Product> product_list;

    private FirebaseFirestore firebaseFirestore;

    private AdminRecyclerAdapter adminRecyclerAdapter;


    public HomeBeheerderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_beheerder, container, false);

        product_list = new ArrayList<>();
        product_list_view = view.findViewById(R.id.product_list_view_beheerder);

        firebaseFirestore = FirebaseFirestore.getInstance();

        adminRecyclerAdapter = new AdminRecyclerAdapter(product_list);
        product_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        product_list_view.setAdapter(adminRecyclerAdapter);

        firebaseFirestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){

                        Product product = doc.getDocument().toObject(Product.class);
                        product_list.add(product);

                        adminRecyclerAdapter.notifyDataSetChanged();



                    }

                }

            }
        });



        return view;

    }


}
