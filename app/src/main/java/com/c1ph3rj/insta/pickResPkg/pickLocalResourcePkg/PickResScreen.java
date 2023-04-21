package com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg;

import static com.c1ph3rj.insta.MainActivity.STORAGE_PERMISSION;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.c1ph3rj.insta.common.FileHelper;
import com.c1ph3rj.insta.common.model.LocalFile;
import com.c1ph3rj.insta.databinding.FragmentPickResScreenBinding;
import com.c1ph3rj.insta.pickResPkg.PickResourceScreen;
import com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.adapter.LocalFilesViewAdapter;
import com.c1ph3rj.insta.utils.permissionsPkg.PermissionHandler;

import java.io.File;
import java.util.ArrayList;

public class PickResScreen extends Fragment {
    FragmentPickResScreenBinding pickResScreenBinding;
    ImageView closeBtn;
    RecyclerView localResView;
    ImageView nextBtn;
    ImageView chooseLocationIcon;
    TextView chooseLocationTitle;
    ImageView imagePreview;
    VideoView videoPreview;
    PickResourceScreen pickResourceScreen;
    ArrayList<LocalFile> listOfFilesInDevice;
    ArrayList<Uri> listOfFilesUri;
    LocalFilesViewAdapter localFilesViewAdapter;

    public PickResScreen() {
        // Required empty public constructor
    }

    public PickResScreen(PickResourceScreen pickResourceScreen) {
        this.pickResourceScreen = pickResourceScreen;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pickResScreenBinding = FragmentPickResScreenBinding.inflate(inflater, container, false);
        return pickResScreenBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    @SuppressLint("NotifyDataSetChanged")
    void init() {
        try {
            PermissionHandler permissionHandler = new PermissionHandler(requireActivity());
            if (permissionHandler.hasPermissions(STORAGE_PERMISSION)) {
                closeBtn = pickResScreenBinding.closeBtn;
                nextBtn = pickResScreenBinding.nextBtn;
                chooseLocationIcon = pickResScreenBinding.chooseLocationIcon;
                chooseLocationTitle = pickResScreenBinding.chooseLocationTitle;
                localResView = pickResScreenBinding.listOfResourcesView;
                imagePreview = pickResScreenBinding.previewImagesView;
                videoPreview = pickResScreenBinding.previewVideoView;
                listOfFilesInDevice = new ArrayList<>();
                listOfFilesUri = new ArrayList<>();

                closeBtn.setOnClickListener(onClickClose -> {
                    try {
                        pickResourceScreen.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                chooseLocationTitle.setOnClickListener(onClickChooseLocation -> {
                    try {
                        // TODO Handle choose location
                        System.out.println("Choose Location Clicked");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                chooseLocationIcon.setOnClickListener(onClickChooseLocation -> chooseLocationTitle.performClick());
            } else {
                permissionHandler.requestPermissions(STORAGE_PERMISSION);
            }

            permissionHandler.setPermissionResultListener(new PermissionHandler.PermissionResultListener() {
                @Override
                public void onPermissionGranted(String permission) {
                    init();
                }

                @Override
                public void onPermissionDenied(String permission) {
                    permissionHandler.showPermissionExplanationDialog();
                }
            });

            try {
                localFilesViewAdapter = new LocalFilesViewAdapter(requireActivity(), listOfFilesInDevice, listOfFilesUri);
                localResView.setAdapter(localFilesViewAdapter);
                localResView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            } catch (Exception e) {
                e.printStackTrace();
            }

            listOfFilesInDevice.addAll(FileHelper.combineMediaFiles(FileHelper.groupMediaFilesByDirectory(requireContext())));



            localFilesViewAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}