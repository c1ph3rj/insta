package com.c1ph3rj.insta.dashboardPkg.bottomNavFragments;

import static com.c1ph3rj.insta.MainActivity.listOfFollowersUuid;
import static com.c1ph3rj.insta.MainActivity.userDetails;

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
import com.c1ph3rj.insta.common.model.UserListModel;
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
    ImageView backBtn;
    ImageView settingsBtn;
    FragmentProfileScreenBinding profileScreenBinding;
    Dashboard dashboard;
    ImageView userProfilePicView;
    TextView postCountView, followersCountView, followingCountView;
    TextView aboutTheUserView;
    RecyclerView listOfUsersView;
    FirebaseFirestore fireStoreDb;
    ArrayList<UserListModel> listOfUsers;
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
            backBtn = profileScreenBinding.backBtn;
            userProfilePicView = profileScreenBinding.userProfilePic;
            followersCountView = profileScreenBinding.followersCount;
            followingCountView = profileScreenBinding.followingCount;
            postCountView = profileScreenBinding.postCount;
            aboutTheUserView = profileScreenBinding.aboutTheUserView;
            listOfUsersView = profileScreenBinding.listOfUsers;
            suggestionsView = profileScreenBinding.suggestionsView;
            userContentTabs = profileScreenBinding.userContentsTabs;
            userContentView = profileScreenBinding.userContentsView;

            fireStoreDb = FirebaseFirestore.getInstance();
            Query getListOfUsers = fireStoreDb.collection("List_Of_Users")
                    .whereNotEqualTo(FieldPath.documentId(), userDetails.getUuid())
                    .limit(50);
            listOfUsers = new ArrayList<>();

            try {
                suggestionsView.setVisibility(View.GONE);
                suggestionsView.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Glide.with(requireActivity())
                        .load(userDetails.getProfilePic())
                        .error(R.drawable.user_ic)
                        .placeholder(R.drawable.user_ic)
                        .circleCrop()
                        .into(userProfilePicView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            backBtn.setOnClickListener(onClickBack -> {
                try {
                    dashboard.homePageView.setCurrentItem(0);
                    dashboard.bottomNavigationView.setSelectedItemId(R.id.home);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            try {
                userNameView.setText(userDetails.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateProfileValues();

            try {
                if (userDetails.getAboutUser().isEmpty()) {
                    aboutTheUserView.setVisibility(View.GONE);
                } else {
                    aboutTheUserView.setText(userDetails.getAboutUser());
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
                            if(task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    UserListModel userListModel = documentSnapshot.toObject(UserListModel.class);
                                    assert userListModel != null;
                                    if (!listOfFollowersUuid.contains(userListModel.getUuid())) {
                                        listOfUsers.add(userListModel);
                                    }
                                }

                                if(listOfUsers.size() != 0){
                                    new Handler()
                                            .postDelayed(() ->{
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
            postCountView.setText(String.valueOf(userDetails.getNoOfPost()));
            followingCountView.setText(String.valueOf(userDetails.getNoOfFollowing()));
            followersCountView.setText(String.valueOf(userDetails.getNoOfFollowers()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}