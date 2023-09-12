package com.musthave0145.mochelins.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHoder> implements Filterable {
    private List<String> unfilterList;
    private SearchFragment context;
    private List<String> filterList;

    private String searchQuery;


    public SearchAdapter(SearchFragment searchFragment, List<String> unfilterList, String searchQuery) {
        this.unfilterList = unfilterList;
        this.context = searchFragment;
        this.filterList = unfilterList;
        this.searchQuery = searchQuery;

    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHoder(LayoutInflater.from(context.getContext()).inflate(R.layout.search_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        String item = filterList.get(position);
        holder.textView.setText(filterList.get(position));

        //검색어와 현재 아이템을 비교하여 검색어가 포함되면 색상을 변경합니다
        if (searchQuery != null) {
            if (item.toLowerCase().contains(searchQuery)) {
                int startPos = item.toLowerCase().indexOf(searchQuery);
                int endPos = startPos + searchQuery.length();

                // SpannableString을 사용하여 색상을 변경합니다
                SpannableString spannableString = new SpannableString(item);
                spannableString.setSpan(new ForegroundColorSpan(Color.rgb(220,0,0)), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.textView.setText(spannableString);
            } else {
                // 검색어가 포함되지 않은 경우 색상을 원래 색으로 설정합니다
                holder.textView.setTextColor(Color.BLACK);
            }
        }
    }
    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String str = constraint.toString();
                if (str.isEmpty()) {
                    filterList = unfilterList;
                } else {
                    List<String> filteringList = new ArrayList<>();

                    for (String item : unfilterList) {
                        if (item.toLowerCase().contains(str))
                            filteringList.add(item);
                    }

                    filterList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;

                return filterResults;
            }

            @Override
            protected void publishResults (CharSequence constraint, FilterResults results){

                filterList = (List<String>) results.values;

            }
        };
    }

    public void setSearchQuery(String query) {
        searchQuery = query.toLowerCase(); // 검색어 업데이트 및 소문자로 변환

        notifyDataSetChanged(); // 검색어가 변경될 때마다 RecyclerView 업데이트
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.searchQuery);
        }
    }
    }

