package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.SearchStore;

import java.util.ArrayList;

public class SearchStoreAdapter  extends RecyclerView.Adapter<SearchStoreAdapter.ViewHolder>{
    Context context;
    ArrayList<SearchStore> searchStoreArrayList;

    public SearchStoreAdapter(Context context, ArrayList<SearchStore> searchStoreArrayList) {
        this.context = context;
        this.searchStoreArrayList= searchStoreArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchstore_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchStore searchStore = searchStoreArrayList.get(position);
        String rating = Double.toString(searchStore.rating);
        holder.txtName.setText(searchStore.storeName);
        holder.txtAddr.setText(searchStore.storeAddr);
        holder.txtSSRating.setText(rating);

    }

    @Override
    public int getItemCount() {
        return searchStoreArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView txtName;
       TextView txtAddr;
       TextView txtSSRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtSSName);
            txtAddr = itemView.findViewById(R.id.txtSSAddr);
            txtSSRating = itemView.findViewById(R.id.txtSSRating);
        }
    }
}
