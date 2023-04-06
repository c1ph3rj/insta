package com.c1ph3rj.insta.dashboardPkg.bottomNavFragments.feedsPkg;

import static com.c1ph3rj.insta.MainActivity.userDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.databinding.FragmentFeedsScreenBinding;
import com.c1ph3rj.insta.utils.badgeIconPkg.ImageBadgeView;

public class FeedsScreen extends Fragment {
    ImageBadgeView notificationBtn;
    ImageBadgeView directBtn;
    FragmentFeedsScreenBinding feedsScreenBinding;
    DashboardScreen dashboardScreen;

    public FeedsScreen(DashboardScreen dashboardScreen) {
        this.dashboardScreen = dashboardScreen;
    }

    public FeedsScreen() {
    }


    /* TODO Saving Instance For Fragment.
    public static FeedsScreen newInstance(String param1, String param2) {
        FeedsScreen fragment = new FeedsScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* TODO Recovering Saved Instance For Fragment
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        feedsScreenBinding = FragmentFeedsScreenBinding.inflate(inflater, container, false);
        return feedsScreenBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    void init() {
        try {
            notificationBtn = feedsScreenBinding.notificationBtn;
            directBtn = feedsScreenBinding.directBtn;

            directBtn.setOnClickListener(onClickDirect -> {
                if (dashboardScreen != null) {
                    dashboardScreen.setPageToDirect();
                }
            });

            checkForNewNotifications();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void checkForNewNotifications(){
        try {
            if(userDetails.isNewMessage()){
                directBtn.setBadgeValue(5);
            }else{
                directBtn.clearBadge();
            }

            if(userDetails.isNewNotification()){
                notificationBtn.setBadgeValue(5);
            }else{
                notificationBtn.clearBadge();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}