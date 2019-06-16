package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.history;

import java.util.Date;
import java.util.List;

public class historyRecyclerAdpter extends RecyclerView.Adapter<historyRecyclerAdpter.ViewHolder> {
    public List<history>history_list;
    private Context context;
    private long millisecondLend;
    private long millisecondReturn;
    private String lendDateString;
    private String returnDateString;

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
        //retrieve product name from database.
        String title_data = history_list.get(i).getProduct();
        viewHolder.setProductTitle(title_data);
        //retrieve timeOFLend from database.
        millisecondLend = history_list.get(i).getTimeOfLend().getTime();
        lendDateString = DateFormat.format("dd/MM/yyyy", new Date(millisecondLend)).toString();
        viewHolder.setLendTimeView(lendDateString);
        //retrieve timeOFReturn from database.
        millisecondReturn = history_list.get(i).getTimeOfReturn().getTime();
        returnDateString = DateFormat.format("dd/MM/yyyy", new Date(millisecondReturn)).toString();
        viewHolder.setReturnTimeView(returnDateString);

    }

    @Override
    public int getItemCount() {
        return history_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView productTitle;
        private TextView returnTimeView;
        private TextView lendTimeView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setProductTitle(String productTitleText){
            productTitle = mView.findViewById(R.id.history_product_title);
            productTitle.setText(productTitleText);

        }
        public  void  setReturnTimeView(String returnTimeText){
            returnTimeView = mView.findViewById(R.id.history_return_time_db);
            returnTimeView.setText(returnTimeText);
        }
        public  void  setLendTimeView(String lendTimeText){
            lendTimeView = mView.findViewById(R.id.history_lend_time_db);
            lendTimeView.setText(lendTimeText);
        }
    }
}
