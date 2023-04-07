package com.c1ph3rj.insta.pickResPkg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.c1ph3rj.insta.databinding.ActivityPickResourceScreenBinding;
import com.c1ph3rj.insta.pickResPkg.adapter.PickResViewAdapter;
import com.c1ph3rj.insta.pickResPkg.captureResourcePkg.CaptureResScreen;
import com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.PickResScreen;
import com.google.android.material.tabs.TabLayout;

public class PickResourceScreen extends AppCompatActivity {
    ViewPager2 pickResView;
    PickResViewAdapter pickResViewAdapter;
    TabLayout pickResViewDecider;
    PickResScreen pickResScreen;
    CaptureResScreen captureResScreen;
    ActivityPickResourceScreenBinding pickResourceScreenBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickResourceScreenBinding = ActivityPickResourceScreenBinding.inflate(getLayoutInflater());
        setContentView(pickResourceScreenBinding.getRoot());

        init();
    }

    void init(){
        try {
            pickResView = pickResourceScreenBinding.pickResourceView;
            pickResViewDecider = pickResourceScreenBinding.pickResourceViewDecider;

            pickResScreen = new PickResScreen();
            captureResScreen = new CaptureResScreen();




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}