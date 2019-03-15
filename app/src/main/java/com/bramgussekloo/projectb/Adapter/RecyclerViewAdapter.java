package com.bramgussekloo.projectb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Product> mProducts;
    private Context mContext;
    private OnProductListener mOnProductListener;

    public RecyclerViewAdapter(Context context, OnProductListener onProductListener, ArrayList<Product> mProducts) {
        this.mProducts = mProducts;
        this.mContext = context;
        this.mOnProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnProductListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mProducts.get(position).getImage())
                .into(holder.image);

        holder.productName.setText(mProducts.get(position).getName());

//        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: ");
//
//                Toast.makeText(mContext, mProducts.get(position).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        CircleImageView image;
        TextView productName;
        RelativeLayout parentLayout;
        OnProductListener onProductListener;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            productName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onProductListener = onProductListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onProductListener.onProductClick(getAdapterPosition());
        }
    }

    public interface OnProductListener{
        void onProductClick(int position);
    }
}
