package com.c1ph3rj.insta.pickResPkg.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class PickResourceViewAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> typesOfPickResList;

    public PickResourceViewAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> typesOfPickResList) {
        super(fragmentActivity);
        this.typesOfPickResList = typesOfPickResList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return typesOfPickResList.get(position);
    }

    @Override
    public int getItemCount() {
        return typesOfPickResList.size();
    }
}
