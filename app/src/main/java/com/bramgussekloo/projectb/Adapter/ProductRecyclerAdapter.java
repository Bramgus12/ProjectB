package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    public List<Product> product_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public int count;

    public ProductRecyclerAdapter(List<Product> product_list){

        this.product_list = product_list;

    }

    @NonNull
    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductRecyclerAdapter.ViewHolder viewHolder, final int i) {

        final String productId = product_list.get(i).productId;
        String title_data = product_list.get(i).getTitle();
        viewHolder.setTitleText(title_data);


        //Get reservations count
        firebaseFirestore.collection("Products/" + productId + "/reservation")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            count = queryDocumentSnapshots.size();
                            int quantity_data = product_list.get(i).getQuantity();
                            viewHolder.setNumInt(quantity_data -count);
                        }else {
                            count = 0;
                            int quantity_data = product_list.get(i).getQuantity();
                            viewHolder.setNumInt(quantity_data -count);
                        }
                    }
                });

        String image_url = product_list.get(i).getImage_url();
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();
        viewHolder.setBlogImage(image_url);

            //change productListReser button text
        firebaseFirestore.collection("Products/" + productId + "/reservation").document(currentUserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){
                    viewHolder.productListReser.setText(R.string.product_reserve_canceled);
                }else {
                    viewHolder.productListReser.setText(R.string.product_reserve);

                }

            }
        });
            //reservations
        viewHolder.productListReser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Products/" + productId + "/reservation").document(currentUserId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){// check if user reserved certain product or not
                            Map<String, Object> reservationsMap = new HashMap<>();
                            reservationsMap.put("timestamp", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("Products/" + productId + "/reservation").document(currentUserId).set(reservationsMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {;
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Product is reserved", Toast.LENGTH_LONG).show();
                                    } else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(context, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {// delete a reservation
                            firebaseFirestore.collection("Products/" + productId + "/reservation").document(currentUserId).delete();

                            }

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView descView;
        private ImageView productImageView;
        private TextView intView;
        private Button productListReser;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            productListReser = mView.findViewById(R.id.product_list_reservations);

        }

        private void setTitleText(String titleText){
            descView = mView.findViewById(R.id.product_list_title);
            descView.setText(titleText);
        }

        private void setNumInt(int numInt){
            intView = mView.findViewById(R.id.product_list_quantity);
            intView.setText(Integer.toString(numInt));
        }

        private void setBlogImage(String downloadUri){
            productImageView = mView.findViewById(R.id.product_list_image);
            Glide.with(context).load(downloadUri).into(productImageView);
        }
    }
}
