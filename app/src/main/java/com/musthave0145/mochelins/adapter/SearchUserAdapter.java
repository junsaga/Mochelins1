package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.SearchUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder>{
    Context context;
    ArrayList<SearchUser> searchUserArrayList;

    Integer[] imgProfileInteger = {R.id.imgSUProfile1, R.id.imgSUProfile2, R.id.imgSUProfile3,
            R.id.imgSUProfile4, R.id.imgSUProfile5, R.id.imgSUProfile6,
            R.id.imgSUProfile7};

    public SearchUserAdapter(Context context, ArrayList<SearchUser> searchUserArrayList) {
        this.context = context;
        this.searchUserArrayList = searchUserArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchuser_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchUser searchUser = searchUserArrayList.get(position);
        holder.txtSUName.setText(searchUser.name);
        Glide.with(context).load(searchUser.profile).fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(holder.SUPoto);
        holder.txtSURating.setText(String.valueOf(searchUser.rating));
        for(int i = 0; i < searchUser.reviews.size(); i++){
            if(i >= holder.imgProfiles.length){
                break;
            }
            holder.imgProfiles[i].setVisibility(View.VISIBLE);
            Glide.with(context).load(searchUser.reviews.get(i).photo).
                    fallback(R.drawable.default_profile).error(R.drawable.default_profile).into(holder.imgProfiles[i]);
        }

    }

    @Override
    public int getItemCount() {
        return searchUserArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtSUName =itemView.findViewById(R.id.txtSUName1);
        ImageView SUPoto =itemView.findViewById(R.id.SUphoto);
        TextView txtSURating = itemView.findViewById(R.id.txtSURating);

        CircleImageView[] imgProfiles = new CircleImageView[imgProfileInteger.length];
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            for (int i = 0; i < imgProfileInteger.length ; i++) {
                imgProfiles[i] = itemView.findViewById(imgProfileInteger[i]);
            }

        }
    }
}
