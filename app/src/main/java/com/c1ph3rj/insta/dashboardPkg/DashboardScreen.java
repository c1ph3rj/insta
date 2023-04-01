package com.c1ph3rj.insta.dashboardPkg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.c1ph3rj.insta.dashboardPkg.adpater.DashboardViewAdapter;
import com.c1ph3rj.insta.dashboardPkg.dashboardFragments.CameraView;
import com.c1ph3rj.insta.dashboardPkg.dashboardFragments.Dashboard;
import com.c1ph3rj.insta.dashboardPkg.dashboardFragments.Directs;
import com.c1ph3rj.insta.databinding.ActivityDashboardScreenBinding;

import java.util.ArrayList;

public class DashboardScreen extends AppCompatActivity {
    ActivityDashboardScreenBinding dashboardScreenBinding;
    Dashboard dashboard;
    Directs directs;
    CameraView cameraView;
    ViewPager2 dashboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardScreenBinding = ActivityDashboardScreenBinding.inflate(getLayoutInflater());
        setContentView(dashboardScreenBinding.getRoot());
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void init(){
        try {
            dashboard = new Dashboard(this);
            cameraView = new CameraView(this);
            directs = new Directs(this);
            dashboardView = dashboardScreenBinding.DashboardView;

            ArrayList<Fragment> listOfDashboardView = new ArrayList<>();
            listOfDashboardView.add(cameraView);
            listOfDashboardView.add(dashboard);
            listOfDashboardView.add(directs);
            DashboardViewAdapter dashboardViewAdapter = new DashboardViewAdapter(DashboardScreen.this, listOfDashboardView);
            dashboardView.setAdapter(dashboardViewAdapter);
            dashboardView.setCurrentItem(1, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageToDirect(){
        dashboardView.setCurrentItem(2, true);
    }

    @Override
    public void onBackPressed() {
        if(dashboardView.getCurrentItem() == 1){
            finishAffinity();
        }else {
            dashboardView.setCurrentItem(1);
        }
    }
}