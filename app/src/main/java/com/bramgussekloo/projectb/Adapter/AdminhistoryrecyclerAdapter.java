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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static java.lang.String.format;

public class AdminhistoryrecyclerAdapter extends RecyclerView.Adapter<AdminhistoryrecyclerAdapter.ViewHolder> {

    public List<Reservation> reservationList;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String title_data = reservationList.get(i).getProduct();
        viewHolder.setTitletext(title_data);

        long millisecond = reservationList.get(i).getTimestamp().getTime();
        String dataString = DateFormat.format("dd/MM/yy",new Date(millisecond)).toString();
        viewHolder.setTime(dataString);

        String user_name = reservationList.get(i).NameId;
        viewHolder.setNameView(user_name);
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
        public void setTitletext(String titletext){
            titleView = mView.findViewById(R.id.history_admin_lend_product_name);
            titleView.setText(titletext);
        }
        public void setTime(String date){
            ReservationDate = mView.findViewById(R.id.history_admin_reserve_date_db);
            ReservationDate.setText(date);
        }
        private void setNameView(String nameText){
            nameView = mView.findViewById(R.id.history_admin_lend_product_name_db);
            nameView.setText(nameText);
        }
    }

}
