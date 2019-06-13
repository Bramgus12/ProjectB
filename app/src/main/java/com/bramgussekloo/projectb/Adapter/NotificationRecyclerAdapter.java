package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;
import com.bramgussekloo.projectb.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {

    private List<Lend> product_list;
    private Context context;
    private OnNotificationClickListener onNotificationClickListener;
    private static final String TAG = "NotificationRecyclerAda";
    private long millisecond;
    private String lendDateString;


    public NotificationRecyclerAdapter(List<Lend> product_list, OnNotificationClickListener onNotificationClickListener) {
        this.product_list = product_list;
        this.onNotificationClickListener = onNotificationClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_return_notifications, parent, false);
        return new ViewHolder(view, onNotificationClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        String product_title = product_list.get(i).getProduct();
        viewHolder.setTitleText(product_title);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("users").child(product_list.get(i).NameId).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.getValue().toString();
                viewHolder.setNameView(user_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        millisecond = product_list.get(i).getTimeOfReturn().getTime();
        lendDateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        viewHolder.setReturnTimeView(lendDateString);

    }

    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View mView;
        private TextView titleView;
        private TextView nameView;
        private TextView returnTimeView;
        private Button notificationButton;
        private OnNotificationClickListener onNotificationClickListener;

        public ViewHolder(@NonNull View itemView, OnNotificationClickListener onNotificationClickListener) {
            super(itemView);
            mView = itemView;
            this.onNotificationClickListener = onNotificationClickListener;

            notificationButton = itemView.findViewById(R.id.return_notification_button);
            notificationButton.setOnClickListener(this);
        }

        private void setTitleText(String titleText) {
            titleView = mView.findViewById(R.id.return_notification_title);
            titleView.setText(titleText);

        }

        private void setNameView(String nameText) {
            nameView = mView.findViewById(R.id.return_notification_name);
            nameView.setText(nameText);
        }

        private void setReturnTimeView(String returnTimeText) {
            returnTimeView = mView.findViewById(R.id.return_notification_more_date);
            returnTimeView.setText(returnTimeText);
        }

        @Override
        public void onClick(View view) {
            onNotificationClickListener.onNotificationClick(getAdapterPosition());

        }
    }

    public interface OnNotificationClickListener{
        void onNotificationClick(int position);
    }
}
