package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.SearchRecent;
import com.musthave0145.mochelins.model.SearchRecentRes;

import java.util.ArrayList;

public class SearchRecentAdapter extends RecyclerView.Adapter<SearchRecentAdapter.ViewHolder> {
    Context context;
    ArrayList<SearchRecent> searchRecentArrayList;

    public SearchRecentAdapter(Context context, ArrayList<SearchRecent> searchRecentArrayList) {
        this.context = context;
        this.searchRecentArrayList = searchRecentArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchrecent_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchRecent searchRecent = searchRecentArrayList.get(position);
        holder.textView.setText(searchRecent.search);
    }

    @Override
    public int getItemCount() {
        return searchRecentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.searchQuery2);

        }
    }
}