package com.c1ph3rj.insta.dashboardPkg.dashboardFragments;

import static com.c1ph3rj.insta.MainActivity.userDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.databinding.FragmentDashbordBinding;
import com.c1ph3rj.insta.utils.badgeIconPkg.ImageBadgeView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends Fragment {
    FragmentDashbordBinding dashboardBinding;
    BottomNavigationView bottomNavigationView;
    ImageBadgeView notificationBtn;
    ImageBadgeView directBtn;
    DashboardScreen dashboardScreen;

    public Dashboard() {
        // Required empty public constructor
    }

    public Dashboard(DashboardScreen dashboardScreen){
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

            setUserProfileToBottomNav();

            notificationBtn = dashboardBinding.notificationBtn;
            directBtn = dashboardBinding.directBtn;

            directBtn.setOnClickListener(onClickDirect -> {
                if(dashboardScreen != null){
                    dashboardScreen.setPageToDirect();
                }
            });
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
                        .load(userDetails.getProfilePic())
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