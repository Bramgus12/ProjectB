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
import com.bramgussekloo.projectb.models.Lend;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdminReservationsRecyclerAdapter extends RecyclerView.Adapter<AdminReservationsRecyclerAdapter.ViewHolder> {

    private List<Lend> lend_list;
    private Context context;

    private static final String TAG = "AdminReservationsRecycl";

    public AdminReservationsRecyclerAdapter(List<Lend> lend_list) {
        this.lend_list = lend_list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reservations_admin, parent, false);

        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String product_title = lend_list.get(i).getProduct();
        viewHolder.setTitleText(product_title);

        String user_name = lend_list.get(i).NameId;
        viewHolder.setNameView(user_name);



    }

    @Override
    public int getItemCount() {
        return lend_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView titleView;
        private TextView nameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        private void setTitleText(String titleText){
            titleView = mView.findViewById(R.id.more_reservations_list_title);
            titleView.setText(titleText);

        }

        private void setNameView(String nameText){
            nameView = mView.findViewById(R.id.more_reservations_list_name);
            nameView.setText(nameText);
        }

    }
}

