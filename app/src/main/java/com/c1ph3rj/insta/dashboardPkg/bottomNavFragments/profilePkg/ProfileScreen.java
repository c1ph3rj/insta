package com.c1ph3rj.insta.dashboardPkg.bottomNavFragments.profilePkg;

import static com.c1ph3rj.insta.MainActivity.listOfFollowersUuid;
import static com.c1ph3rj.insta.MainActivity.userModelDetails;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.common.adpater.ListOfUsersAdapter;
import com.c1ph3rj.insta.common.model.FriendsModel;
import com.c1ph3rj.insta.dashboardPkg.dashboardFragments.Dashboard;
import com.c1ph3rj.insta.databinding.FragmentProfileScreenBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ProfileScreen extends Fragment {
    TextView userNameView;
    ImageView settingsBtn;
    ImageView privateIcon;
    FragmentProfileScreenBinding profileScreenBinding;
    Dashboard dashboard;
    ImageView userProfilePicView;
    TextView postCountView, followersCountView, followingCountView;
    TextView aboutTheUserView;
    RecyclerView listOfUsersView;
    FirebaseFirestore fireStoreDb;
    TextView viewAllSuggestionsBtn;
    ArrayList<FriendsModel> listOfUsers;
    ListOfUsersAdapter listOfUsersAdapter;
    LinearLayout suggestionsView;
    TabLayout userContentTabs;
    ViewPager2 userContentView;

    public ProfileScreen() {
        // Required empty public constructor
    }

    public ProfileScreen(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileScreenBinding = FragmentProfileScreenBinding.inflate(inflater, container, false);
        return profileScreenBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    @SuppressLint("NotifyDataSetChanged")
    void init() {
        try {
            userNameView = profileScreenBinding.profileUserNameView;
            settingsBtn = profileScreenBinding.settingsBtn;
            userProfilePicView = profileScreenBinding.userProfilePic;
            followersCountView = profileScreenBinding.followersCount;
            followingCountView = profileScreenBinding.followingCount;
            postCountView = profileScreenBinding.postCount;
            aboutTheUserView = profileScreenBinding.aboutTheUserView;
            listOfUsersView = profileScreenBinding.listOfUsers;
            suggestionsView = profileScreenBinding.suggestionsView;
            userContentTabs = profileScreenBinding.userContentsTabs;
            userContentView = profileScreenBinding.userContentsView;
            privateIcon = profileScreenBinding.privateIcon;
            viewAllSuggestionsBtn = profileScreenBinding.viewAllSuggestionBtn;

            fireStoreDb = FirebaseFirestore.getInstance();
            Query getListOfUsers = fireStoreDb.collection("List_Of_Users")
                    .whereNotEqualTo(FieldPath.documentId(), userModelDetails.getUuid())
                    .limit(50);
            listOfUsers = new ArrayList<>();

            int [] listOfTabIcons = new int[] {R.drawable.post_ic, R.drawable.video_ic_filled, R.drawable.tag_ic};
            int [] listOfTabIds = new int[] {101, 102, 103};

            try {
                for(int i = 0; i < listOfTabIcons.length; i++){
                    TabLayout.Tab tabItem = userContentTabs.newTab();
                    tabItem.setIcon(listOfTabIcons[i]);
                    tabItem.setId(listOfTabIds[i]);
                    userContentTabs.addTab(tabItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                suggestionsView.setVisibility(View.GONE);
                suggestionsView.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Glide.with(requireActivity())
                        .load(userModelDetails.getProfilePic())
                        .error(R.drawable.user_ic)
                        .placeholder(R.drawable.user_ic)
                        .circleCrop()
                        .into(userProfilePicView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if(userModelDetails.isAccountPrivate()){
                    privateIcon.setVisibility(View.VISIBLE);
                }else{
                    privateIcon.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                userNameView.setText(userModelDetails.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateProfileValues();

            try {
                if (userModelDetails.getAboutUser().isEmpty()) {
                    aboutTheUserView.setVisibility(View.GONE);
                } else {
                    aboutTheUserView.setText(userModelDetails.getAboutUser());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                listOfUsersAdapter = new ListOfUsersAdapter(requireActivity(), listOfUsers, this);
                listOfUsersView.setAdapter(listOfUsersAdapter);
                listOfUsersView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                getListOfUsers.get().addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    FriendsModel friendsModel = documentSnapshot.toObject(FriendsModel.class);
                                    assert friendsModel != null;
                                    if (!listOfFollowersUuid.contains(friendsModel.getUuid())) {
                                        listOfUsers.add(friendsModel);
                                    }
                                }

                                if (listOfUsers.size() != 0) {
                                    new Handler()
                                            .postDelayed(() -> {
                                                if(listOfUsers.size() < 15){
                                                    viewAllSuggestionsBtn.setVisibility(View.GONE);
                                                }
                                                listOfUsersAdapter.notifyDataSetChanged();
                                                suggestionsView.setVisibility(View.VISIBLE);
                                                TransitionManager.beginDelayedTransition((ViewGroup) suggestionsView.getParent());
                                            }, 3000);
                                }
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateProfileValues() {
        try {
            postCountView.setText(String.valueOf(userModelDetails.getNoOfPost()));
            followingCountView.setText(String.valueOf(userModelDetails.getNoOfFollowing()));
            followersCountView.setText(String.valueOf(userModelDetails.getNoOfFollowers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}