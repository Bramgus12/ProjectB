package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {

    public List<Product> product_list;
    public ProductRecyclerAdapter(List<Product> product_list){

        this.product_list = product_list;

    }

    @NonNull
    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecyclerAdapter.ViewHolder viewHolder, int i) {

        String desc_data = product_list.get(i).getDesc();
        viewHolder.setDescText(desc_data);

    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView descView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;


        }

        private void setDescText(String descText){
            descView = mView.findViewById(R.id.product_list_desc);
            descView.setText(descText);
        }
    }
}
