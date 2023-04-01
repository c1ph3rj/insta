package com.c1ph3rj.insta.dashboardPkg.dashboardFragments;

import static com.c1ph3rj.insta.MainActivity.isDeviceIsInNightMode;
import static com.c1ph3rj.insta.MainActivity.userDetails;

import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.databinding.FragmentDashbordBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends Fragment {
    FragmentDashbordBinding dashboardBinding;
    BottomNavigationView bottomNavigationView;
    MenuItem userProfile;

    public Dashboard() {
        // Required empty public constructor
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

    void init() {
        try {
            bottomNavigationView = dashboardBinding.bottomNavView;
            Menu bottomNavMenu = bottomNavigationView.getMenu();
            userProfile = bottomNavMenu.findItem(R.id.profile);

            try {
                isDeviceIsInNightMode(requireActivity());
                Glide.with(requireActivity())
                        .load(userDetails.getProfilePic())
                        .circleCrop()
                        .error(R.drawable.user_ic)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                userProfile.setIcon(resource);
                                userProfile.setIconTintList(null);
                                MenuItemCompat.setIconTintMode(userProfile, PorterDuff.Mode.DST_OVER);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                userProfile.setIconTintList(null);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}