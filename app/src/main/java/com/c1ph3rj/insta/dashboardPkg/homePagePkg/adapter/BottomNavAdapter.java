package com.c1ph3rj.insta.dashboardPkg.homePagePkg.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class BottomNavAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> listOfHomePageViews;

    public BottomNavAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> listOfHomePageViews) {
        super(fragmentActivity);
        this.listOfHomePageViews = listOfHomePageViews;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listOfHomePageViews.get(position);
    }

    @Override
    public int getItemCount() {
        return listOfHomePageViews.size();
    }
}
