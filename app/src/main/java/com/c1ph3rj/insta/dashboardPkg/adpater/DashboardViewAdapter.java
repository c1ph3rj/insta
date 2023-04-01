package com.c1ph3rj.insta.dashboardPkg.adpater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class DashboardViewAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> listOfDashboardView;

    public DashboardViewAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> listOfDashboardView) {
        super(fragmentActivity);
        this.listOfDashboardView = listOfDashboardView;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return listOfDashboardView.get(position);
    }

    @Override
    public int getItemCount() {
        return listOfDashboardView.size();
    }
}
