package com.example.fooddelivery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder>
{

    private List<Item> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerviewAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row rakepolicy.xml from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.categorycard, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String category = mData.get(position).getCategory();
        Uri uri = Uri.parse(mData.get(position).getImage());
        String description = mData.get(position).getDescription();
        // String brief = mData.get(position).getBrief();
        holder.title_view.setText(category);
        holder.description_view.setText(description);


        Picasso.get().load(uri).fit().centerCrop().into(holder.category_imageview);


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(Home blogsFragment) {
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title_view, description_view;
        ImageView category_imageview;

        ViewHolder(View itemView) {
            super(itemView);
            title_view = itemView.findViewById(R.id.category_rv);
            category_imageview = itemView.findViewById(R.id.category_image);
            description_view = itemView.findViewById(R.id.description_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
