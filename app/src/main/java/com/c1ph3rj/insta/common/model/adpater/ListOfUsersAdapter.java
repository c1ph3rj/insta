package com.c1ph3rj.insta.common.model.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.common.model.UserListModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class ListOfUsersAdapter extends RecyclerView.Adapter<ListOfUsersAdapter.ViewHolder> {
    Context context;
    ArrayList<UserListModel> listOfUsers;

    public ListOfUsersAdapter(Context context, ArrayList<UserListModel> listOfUsers){
        this.listOfUsers = listOfUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_of_users_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            UserListModel userListModel = listOfUsers.get(position);
            try {
                holder.userNameView.setText(userListModel.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Glide.with(context)
                        .load(userListModel.getProfilePic())
                        .placeholder(R.drawable.user_ic)
                        .error(R.drawable.user_ic)
                        .into(holder.userProfilePicView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userNameView;
        TextView followedBtnTxt;
        MaterialButton followBtn;
        CardView followedView;
        ImageView userProfilePicView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameView = itemView.findViewById(R.id.userNameView);
            followedBtnTxt = itemView.findViewById(R.id.followedBtnTxt);
            followBtn = itemView.findViewById(R.id.followBtn);
            followedView = itemView.findViewById(R.id.followedBtn);
            userProfilePicView = itemView.findViewById(R.id.userProfilePicView);
        }
    }
}
