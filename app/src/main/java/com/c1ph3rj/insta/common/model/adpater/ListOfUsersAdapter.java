package com.c1ph3rj.insta.common.model.adpater;

import static com.c1ph3rj.insta.MainActivity.displayToast;
import static com.c1ph3rj.insta.MainActivity.userDetails;

import android.app.Activity;
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
import com.c1ph3rj.insta.MainActivity;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.common.model.UserListModel;
import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.dashboardPkg.dashboardFragments.Dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListOfUsersAdapter extends RecyclerView.Adapter<ListOfUsersAdapter.ViewHolder> {
    Context context;
    ArrayList<UserListModel> listOfUsers;
    ArrayList<Boolean> listOfFollowBtnProcess;

    public ListOfUsersAdapter(Context context, ArrayList<UserListModel> listOfUsers, Dashboard dashboard){
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
            try {
                holder.followBtn.setOnClickListener(onCLickFollow ->{
                    isFollowBtnClicked = true;
                    followTheUser(userListModel, holder);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void followTheUser(UserListModel friendModel, ViewHolder holder){
        FirebaseFirestore fireStoreDb = FirebaseFirestore.getInstance();
        DocumentReference userDetailsRef = fireStoreDb.collection("Users")
                .document(userDetails.getUuid());

        userDetailsRef.collection("followers")
                .document(friendModel.getUuid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot friendDetailsRef = task.getResult();
                        if (friendDetailsRef.exists()) {
                            userDetailsRef.collection("followers")
                                    .document(friendModel.getUuid())
                                    .delete();
                            Map<String, Object> noOfFollowers = new HashMap<>();
                            userDetails.setNoOfFollowers(userDetails.getNoOfFollowers() - 1);
                            noOfFollowers.put("noOfFollowers", userDetails.getNoOfFollowers());
                            userDetailsRef.update(noOfFollowers);
                        } else {
                            List<Task<?>> listOfTasks = new ArrayList<>();
                            Task<Void> addFriendRef = userDetailsRef.collection("followers")
                                    .document(friendModel.getUuid())
                                    .set(friendModel);
                            Map<String, Object> noOfFollowers = new HashMap<>();
                            userDetails.setNoOfFollowers(userDetails.getNoOfFollowers() + 1);
                            noOfFollowers.put("noOfFollowers", userDetails.getNoOfFollowers());
                            Task<Void> increaseFollowersCountRef = userDetailsRef.update(noOfFollowers);
                            listOfTasks.add(addFriendRef);
                            listOfTasks.add(increaseFollowersCountRef);

                            Tasks.whenAll(listOfTasks)
                                    .addOnCompleteListener(addedFriend -> {
                                        if(addedFriend.isSuccessful()){
                                            holder.followedView.setVisibility(View.VISIBLE);
                                            holder.followBtn.setVisibility(View.GONE);
                                            holder.followedBtnTxt.setText("Followed");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        displayToast("Something Went Wrong!", context);
                                        holder.followedView.setVisibility(View.GONE);
                                        holder.followBtn.setVisibility(View.VISIBLE);
                                    });
                        }
                    }
                });
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
