package com.c1ph3rj.insta.pickResPkg;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.c1ph3rj.insta.databinding.ActivityPickResourceScreenBinding;
import com.c1ph3rj.insta.pickResPkg.adapter.PickResourceViewAdapter;
import com.c1ph3rj.insta.pickResPkg.captureResourcePkg.CaptureLiveResScreen;
import com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.PickResScreen;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class PickResourceScreen extends AppCompatActivity {
    ViewPager2 pickResView;
    PickResourceViewAdapter pickResourceViewAdapter;
    TabLayout pickResViewDecider;
    PickResScreen pickResScreen;
    CaptureLiveResScreen captureLiveResScreen;
    ActivityPickResourceScreenBinding pickResourceScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickResourceScreenBinding = ActivityPickResourceScreenBinding.inflate(getLayoutInflater());
        setContentView(pickResourceScreenBinding.getRoot());

        init();
    }

    void init() {
        try {
            pickResView = pickResourceScreenBinding.pickResourceView;
            pickResViewDecider = pickResourceScreenBinding.pickResourceViewDecider;

            pickResScreen = new PickResScreen(this);
            captureLiveResScreen = new CaptureLiveResScreen(this);

            ArrayList<Fragment> typesOfResList = new ArrayList<>();
            typesOfResList.add(pickResScreen);
            typesOfResList.add(captureLiveResScreen);

            pickResourceViewAdapter = new PickResourceViewAdapter(this, typesOfResList);

            pickResView.setAdapter(pickResourceViewAdapter);

            pickResView.setUserInputEnabled(false);

            TabLayout.Tab galleryTab = pickResViewDecider.newTab();
            galleryTab.setText("Gallery");
            TabLayout.Tab photosTab = pickResViewDecider.newTab();
            photosTab.setText("Photo");
            TabLayout.Tab videosTab = pickResViewDecider.newTab();
            videosTab.setText("Video");

            pickResViewDecider.addTab(galleryTab);
            pickResViewDecider.addTab(photosTab);
            pickResViewDecider.addTab(videosTab);

            pickResViewDecider.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (Objects.requireNonNull(tab.getText()).toString().equals("Gallery")) {
                        pickResView.setCurrentItem(0);
                    } else if (Objects.requireNonNull(tab.getText()).toString().equals("Photo")) {
                        pickResView.setCurrentItem(1);
                        captureLiveResScreen.switchMode(1);
                    } else if (Objects.requireNonNull(tab.getText()).toString().equals("Video")) {
                        pickResView.setCurrentItem(1);
                        captureLiveResScreen.switchMode(2);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}