package com.c1ph3rj.insta.pickResPkg.captureResourcePkg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.pickResPkg.PickResourceScreen;


public class CaptureLiveResScreen extends Fragment {
    PickResourceScreen pickResourceScreen;

    public CaptureLiveResScreen() {
        // Required empty public constructor
    }

    public CaptureLiveResScreen(PickResourceScreen pickResourceScreen) {
        this.pickResourceScreen = pickResourceScreen;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_res_screen, container, false);
    }

    public void switchMode(int mode){
        if(mode == 1){
            // TODO Code for capture image.
        }else if(mode == 2){
            // TODO code for capture video.
        }
    }
}