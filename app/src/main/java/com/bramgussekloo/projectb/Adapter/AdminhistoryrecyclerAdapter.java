package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bramgussekloo.projectb.Activities.LendActivity;
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
    private OnDeleteListener mOnDeleteListener;
    private OnLendClick monLendClick;
    private Context context;



    public AdminhistoryrecyclerAdapter(List<Reservation> reservationList,OnDeleteListener onDeleteListener, OnLendClick onLendClick){
        this.reservationList = reservationList;
        this.mOnDeleteListener = onDeleteListener;
        this.monLendClick = onLendClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_admin, parent, false);
        context = parent.getContext();


        return new ViewHolder(view,mOnDeleteListener, monLendClick);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnDeleteListener onDeleteListener;
        OnLendClick onLendClick;
        private View mView;
        private TextView titleView;
        private TextView ReservationDate;
        private TextView nameView;
        private Button DeleteButton;
        private Button LendButton;



        public ViewHolder(@NonNull View itemView,OnDeleteListener onDeleteListener, OnLendClick onLendClick) {
            super(itemView);
            mView = itemView;
            this.onDeleteListener = onDeleteListener;
            this.onLendClick = onLendClick;
            DeleteButton = mView.findViewById(R.id.history_admin_delete_lend_button);
            LendButton = mView.findViewById(R.id.history_admin_lend_button);
            DeleteButton.setOnClickListener(this);
            LendButton.setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.history_admin_delete_lend_button:
                    onDeleteListener.onDeleteClick(getAdapterPosition());
                    break;
                case R.id.history_admin_lend_button:
                    onLendClick.onLendClick(getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }
    public interface OnDeleteListener{
        void onDeleteClick(int posistion);

    }
    public interface OnLendClick{
        void onLendClick(int position);
    }

}
