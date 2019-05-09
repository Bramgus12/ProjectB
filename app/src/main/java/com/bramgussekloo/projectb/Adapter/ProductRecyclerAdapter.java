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
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import javax.annotation.Nullable;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    private static final String TAG = "ProductRecyclerAdapter";

    public List<Product> product_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public int count;

    private OnProductListener onProductListener;

    public ProductRecyclerAdapter(List<Product> product_list, OnProductListener onProductListener){

        this.product_list = product_list;
        this.onProductListener = onProductListener;

    }

    @NonNull
    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view, onProductListener);
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
                            if(quantity_data == 0){
                                Toast.makeText(context, "The Product Is Out of Stock", Toast.LENGTH_SHORT).show();
                            } else {
                                viewHolder.setNumInt(quantity_data - count);
                            }
                        } else {
                            count = 0;
                            int quantity_data = product_list.get(i).getQuantity();
                            Log.d(TAG, "onEvent: The Quantity Is Now " + quantity_data);
                            viewHolder.setNumInt(quantity_data - count);
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

                            int quantity_data = product_list.get(i).getQuantity();

                            if(quantity_data != 0){
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
                            }
                            else {
                                Toast.makeText(context, "Product is Out Of Stock!", Toast.LENGTH_SHORT).show();


                            }
                            } else { // delete a reservation
                           
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View mView;
        private TextView descView;
        private ImageView productImageView;
        private TextView intView;
        private Button productListReser;
        OnProductListener onProductListener;
        public Button readMoreButton;



        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            mView = itemView;
            productListReser = mView.findViewById(R.id.more_product_list_reservations);
            this.onProductListener = onProductListener;
            readMoreButton = mView.findViewById(R.id.more_product_list_read_more);

            readMoreButton.setOnClickListener(this);

        }

        private void setTitleText(String titleText){
            descView = mView.findViewById(R.id.more_product_list_title);
            descView.setText(titleText);
        }

        private void setNumInt(int numInt){
            intView = mView.findViewById(R.id.more_product_list_quantity);
            intView.setText(Integer.toString(numInt));
        }

        private void setBlogImage(String downloadUri){
            productImageView = mView.findViewById(R.id.more_product_list_image);
            Glide.with(context).load(downloadUri).into(productImageView);
        }


        @Override
        public void onClick(View view) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductListener{
        void onProductClick(int position);
    }

}
