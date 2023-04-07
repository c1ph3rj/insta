package com.c1ph3rj.insta.dashboardPkg.dashboardFragments;

import static com.c1ph3rj.insta.MainActivity.userModelDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.dashboardPkg.bottomNavFragments.feedsPkg.FeedsScreen;
import com.c1ph3rj.insta.dashboardPkg.bottomNavFragments.profilePkg.ProfileScreen;
import com.c1ph3rj.insta.dashboardPkg.bottomNavFragments.reelsPkg.ReelsScreen;
import com.c1ph3rj.insta.dashboardPkg.bottomNavFragments.searchPkg.SearchScreen;
import com.c1ph3rj.insta.dashboardPkg.homePagePkg.adapter.BottomNavAdapter;
import com.c1ph3rj.insta.databinding.FragmentDashbordBinding;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class Dashboard extends Fragment {
    public BottomNavigationView bottomNavigationView;
    public ViewPager2 homePageView;
    FragmentDashbordBinding dashboardBinding;
    DashboardScreen dashboardScreen;
    int selectedBottomNavPosition = R.id.home;

    public Dashboard() {
        // Required empty public constructor
    }

    public Dashboard(DashboardScreen dashboardScreen) {
        this.dashboardScreen = dashboardScreen;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboardBinding = FragmentDashbordBinding.inflate(inflater, container, false);
        return dashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    @SuppressLint("RestrictedApi")
    void init() {
        try {
            bottomNavigationView = dashboardBinding.bottomNavView;
            homePageView = dashboardBinding.homePageView;

            setUserProfileToBottomNav();

            ArrayList<Fragment> listOfHomePageView = new ArrayList<>();
            FeedsScreen feedsScreen = new FeedsScreen(dashboardScreen);
            SearchScreen searchScreen = new SearchScreen();
            ReelsScreen reelsScreen = new ReelsScreen();
            ProfileScreen profileScreen = new ProfileScreen(this);
            listOfHomePageView.add(feedsScreen);
            listOfHomePageView.add(searchScreen);
            listOfHomePageView.add(reelsScreen);
            listOfHomePageView.add(profileScreen);

            BottomNavAdapter bottomNavAdapter = new BottomNavAdapter(requireActivity(), listOfHomePageView);
            homePageView.setAdapter(bottomNavAdapter);
            homePageView.setUserInputEnabled(false);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                if (item.getItemId() == R.id.home) {
                    selectedBottomNavPosition = item.getItemId();
                    homePageView.setCurrentItem(0);
                } else if (item.getItemId() == R.id.search) {
                    selectedBottomNavPosition = item.getItemId();
                    homePageView.setCurrentItem(1);
                } else if (item.getItemId() == R.id.reels) {
                    selectedBottomNavPosition = item.getItemId();
                    homePageView.setCurrentItem(2);
                } else if (item.getItemId() == R.id.profile) {
                    selectedBottomNavPosition = item.getItemId();
                    homePageView.setCurrentItem(3);
                } else if (item.getItemId() == R.id.addPost) {
                    showBottomSheetDialog();
                }
                dashboardScreen.dashboardView.setUserInputEnabled((item.getItemId() == R.id.home) || (item.getItemId() == R.id.addPost));
                return true;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showBottomSheetDialog() {
        try {
            BottomSheetDialog addPostDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog);
            View view = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.add_post_layout,null);
            addPostDialog.setContentView(view);

            LinearLayout postVideosBtn = view.findViewById(R.id.postVideosBtn);
            LinearLayout postPhotosBtn = view.findViewById(R.id.postPhotosBtn);
            LinearLayout postStoryBtn = view.findViewById(R.id.postStoryBtn);

            postVideosBtn.setOnClickListener(onClickPostVideos ->{
                try {
                    addPostDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            postPhotosBtn.setOnClickListener(onClickPostVideos ->{
                try {
                    addPostDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            postStoryBtn.setOnClickListener(onClickPostVideos ->{
                try {
                    addPostDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });


            addPostDialog.show();
            addPostDialog.setOnDismissListener(dialog ->
                    bottomNavigationView.setSelectedItemId(selectedBottomNavPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUserProfileToBottomNav() {
        try {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
            View views = menuView.getChildAt(4);
            BottomNavigationItemView itemView = (BottomNavigationItemView) views;
            View newView = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.bottom_nav_item, bottomNavigationView, false);
            ImageView userPro = newView.findViewById(R.id.userProfileView);

            try {
                Glide.with(requireActivity())
                        .load(userModelDetails.getProfilePic())
                        .circleCrop()
                        .into(userPro);
                itemView.addView(userPro);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}