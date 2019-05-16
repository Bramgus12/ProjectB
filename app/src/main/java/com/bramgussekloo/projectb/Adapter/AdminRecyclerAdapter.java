package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bramgussekloo.projectb.Activities.LendActivity;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.fragments.HomeAdminFragment;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.ViewHolder> {


    private List<Product> product_list;
    private Context context;
    private String productId;
    private OnProductListenerAdmin onProductListenerAdmin;



    public AdminRecyclerAdapter(List<Product> product_list, OnProductListenerAdmin onProductListenerAdmin){
        this.product_list = product_list;
        this.onProductListenerAdmin = onProductListenerAdmin;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_admin_list_item, parent, false);

        context = parent.getContext();
        return new ViewHolder(view, onProductListenerAdmin);

    }


    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        String title_data = product_list.get(i).getTitle();
        viewHolder.setTitleText(title_data);

        int quantity_data = product_list.get(i).getQuantity();
        viewHolder.setNumInt(quantity_data);

        String image_url = product_list.get(i).getImage_url();
        viewHolder.setBlogImage(image_url);

    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View mView;
        private TextView descView;
        private ImageView productImageView;
        private TextView intView;
        OnProductListenerAdmin onProductListenerAdmin;


        public ViewHolder(@NonNull View itemView, OnProductListenerAdmin onProductListenerAdmin) {
            super(itemView);
            this.onProductListenerAdmin = onProductListenerAdmin;
            mView = itemView;
            Button LendButton = mView.findViewById(R.id.lend_button);
            LendButton.setOnClickListener(this);
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
        public void onClick(View v) {
            onProductListenerAdmin.onProductClickAdmin(getAdapterPosition());
        }
    }
    public interface OnProductListenerAdmin{
        void onProductClickAdmin(int position);
    }

}