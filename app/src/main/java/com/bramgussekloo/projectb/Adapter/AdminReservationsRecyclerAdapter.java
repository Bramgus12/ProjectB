package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdminReservationsRecyclerAdapter extends RecyclerView.Adapter<AdminReservationsRecyclerAdapter.ViewHolder> {

    private List<Lend> lend_list;
    private Context context;
    private OnReturnListener mOnReturnListener;
    private static final String TAG = "AdminReservationsRecycl";

    public AdminReservationsRecyclerAdapter(List<Lend> lend_list, OnReturnListener onReturnListener) {
        this.lend_list = lend_list;
        this.mOnReturnListener = onReturnListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_reservations_admin, parent, false);

        context = parent.getContext();
        return new ViewHolder(view, mOnReturnListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        String product_title = lend_list.get(i).getProduct();
        viewHolder.setTitleText(product_title);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("users").child(lend_list.get(i).NameId).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.getValue().toString();
                viewHolder.setNameView(user_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String day = Integer.toString(lend_list.get(i).getDay());
        String month = Integer.toString(lend_list.get(i).getMonth());
        String year = Integer.toString(lend_list.get(i).getYear());

        String timestamp = day + "/" + month + "/" + year;
        viewHolder.setReturnTimeView(timestamp);







    }

    @Override
    public int getItemCount() {
        return lend_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        OnReturnListener onReturnListener;
        private View mView;
        private TextView titleView;
        private TextView nameView;
        private TextView returnTimeView;
        private Button ReturnButton;

        public ViewHolder(@NonNull View itemView, OnReturnListener onReturnListener) {
            super(itemView);
            mView = itemView;
            this.onReturnListener = onReturnListener;

            ReturnButton = mView.findViewById(R.id.more_reservations_list_title_return);
            ReturnButton.setOnClickListener(this);

        }

        private void setTitleText(String titleText){
            titleView = mView.findViewById(R.id.more_reservations_list_title);
            titleView.setText(titleText);

        }

        private void setNameView(String nameText){
            nameView = mView.findViewById(R.id.more_reservations_list_name);
            nameView.setText(nameText);
        }

        private void setReturnTimeView(String returnTimeText){
            returnTimeView = mView.findViewById(R.id.more_reservations_date);
            returnTimeView.setText(returnTimeText);
        }

        @Override
        public void onClick(View v) {
            onReturnListener.onRturnClick(getAdapterPosition());
        }
    }
    public interface OnReturnListener{
        void onRturnClick(int posistion);
    }
}


