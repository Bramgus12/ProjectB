package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdminRecyclerAdapter extends RecyclerView.Adapter<AdminRecyclerAdapter.ViewHolder>{


    private List<Product> product_list;
    private Context context;


    public AdminRecyclerAdapter(List<Product> product_list){

        this.product_list = product_list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_admin_list_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

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

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView descView;
        private ImageView productImageView;

        private TextView intView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;


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
    }

}