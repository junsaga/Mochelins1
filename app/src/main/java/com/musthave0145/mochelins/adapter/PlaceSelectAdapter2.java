package com.musthave0145.mochelins.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.PlaceSelect;


import java.util.ArrayList;

public class PlaceSelectAdapter2 extends RecyclerView.Adapter<PlaceSelectAdapter2.ViewHolder>{

    Context context;
    ArrayList<PlaceSelect> placeSelectArrayList;



    public int selectedItem = RecyclerView.NO_POSITION;

    public PlaceSelectAdapter2(Context context, ArrayList<PlaceSelect> placeSelectArrayList) {
        this.context = context;
        this.placeSelectArrayList = placeSelectArrayList;
    }


    @NonNull
    @Override
    public PlaceSelectAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_select_row2, parent, false);

        return new PlaceSelectAdapter2.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceSelectAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PlaceSelect placeSelect = placeSelectArrayList.get(position);
        if (placeSelect.storeName == null){
            holder.txtNewAddress.setText("상점명 없음");
        } else {
            holder.txtStoreName.setText(placeSelect.storeName);
        }
        if (placeSelect.storeAddr == null) {
            holder.txtNewAddress.setText("주소 없음");
        } else {
            holder.txtNewAddress.setText(placeSelect.storeAddr);
        }

        // 유저가 선택한 카드뷰에 테두리를 씌워주자!
        if (position == selectedItem) {
            holder.cardView.setBackgroundResource(R.drawable.corner);
        } else {
            holder.cardView.setBackgroundResource(android.R.color.transparent);
        }

        // 서
        holder.cardView.setOnClickListener(v -> {

            if (selectedItem != position) {
                int previousSelectedItem = selectedItem;
                selectedItem = position;
                notifyItemChanged(previousSelectedItem);
                notifyItemChanged(selectedItem);
                PlaceSelect selectedItemData = placeSelectArrayList.get(selectedItem);
            } else {

                int previousSelectedItem = selectedItem;
                selectedItem = RecyclerView.NO_POSITION;
                notifyItemChanged(previousSelectedItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return placeSelectArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtStoreName;
        TextView txtNewAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtStoreName = itemView.findViewById(R.id.txtStoreName);
            txtNewAddress = itemView.findViewById(R.id.txtNewAddress);


        }
    }

}
