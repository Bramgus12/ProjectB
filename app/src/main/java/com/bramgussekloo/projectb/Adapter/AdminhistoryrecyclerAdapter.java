package com.bramgussekloo.projectb.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.List;

public class AdminhistoryrecyclerAdapter extends RecyclerView.Adapter<AdminhistoryrecyclerAdapter.ViewHolder> {

    public List<Reservation> reservationList;
    private DatabaseReference mDatabase;


    public AdminhistoryrecyclerAdapter(List<Reservation> reservationList){
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_admin, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        //product title
        String title_data = reservationList.get(i).getProduct();
        viewHolder.setTitletext(title_data);
        //time of reservation
        long millisecond = reservationList.get(i).getTimestamp().getTime();
        String dataString = DateFormat.format("dd/MM/yy",new Date(millisecond)).toString();
        viewHolder.setTime(dataString);
        //username
        final String user_id = reservationList.get(i).NameId;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users/"+ user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Username = dataSnapshot.child("Name").getValue().toString();
                viewHolder.setNameView(Username);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {

        return reservationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView titleView;
        private TextView ReservationDate;
        private TextView nameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitletext(String titletext){//get product name
            titleView = mView.findViewById(R.id.history_admin_lend_product_name);
            titleView.setText(titletext);
        }
        public void setTime(String date){//get reservation date
            ReservationDate = mView.findViewById(R.id.history_admin_reserve_date_db);
            ReservationDate.setText(date);
        }
        private void setNameView(String nameText){//get reservation time
            nameView = mView.findViewById(R.id.history_admin_lend_product_name_db);
            nameView.setText(nameText);
        }
    }

}