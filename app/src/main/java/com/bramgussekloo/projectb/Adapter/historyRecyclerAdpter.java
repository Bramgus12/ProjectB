package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.history;

import java.util.List;

public class historyRecyclerAdpter extends RecyclerView.Adapter<historyRecyclerAdpter.ViewHolder> {
    public List<history>history_list;
    private Context context;
    public historyRecyclerAdpter(List<history>history_list){
        this.history_list =history_list;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_history_user, viewGroup, false);

        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title_data = history_list.get(i).getProduct();
        viewHolder.setProductTitle(title_data);

    }

    @Override
    public int getItemCount() {
        return history_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView productTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setProductTitle(String productTitleText){
            productTitle = mView.findViewById(R.id.history_product_title);
            productTitle.setText(productTitleText);

        }
    }
}
