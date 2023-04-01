package com.c1ph3rj.insta.dashboardPkg.dashboardFragments;

import static com.c1ph3rj.insta.MainActivity.userDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.c1ph3rj.insta.dashboardPkg.DashboardScreen;
import com.c1ph3rj.insta.databinding.FragmentDirectsBinding;

public class Directs extends Fragment {
    DashboardScreen dashboardScreen;
    FragmentDirectsBinding directsBinding;
    TextView userNameView;

    public Directs() {
        // Required empty public constructor
    }

    public Directs(DashboardScreen dashboardScreen) {
        this.dashboardScreen = dashboardScreen;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        directsBinding = FragmentDirectsBinding.inflate(inflater, container, false);
        return directsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    void init() {
        try {
            userNameView = directsBinding.directUserNameView;

            try {
                userNameView.setText(userDetails.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}