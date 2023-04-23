package com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg;

import static com.c1ph3rj.insta.MainActivity.STORAGE_PERMISSION;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.common.FileHelper;
import com.c1ph3rj.insta.common.model.LocalFile;
import com.c1ph3rj.insta.databinding.FragmentPickResScreenBinding;
import com.c1ph3rj.insta.pickResPkg.PickResourceScreen;
import com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.adapter.LocalFilesViewAdapter;
import com.c1ph3rj.insta.utils.glideSupportPkg.GlideApp;
import com.c1ph3rj.insta.utils.permissionsPkg.PermissionHandler;

import java.io.File;
import java.util.ArrayList;

public class PickResScreen extends Fragment {
    FragmentPickResScreenBinding pickResScreenBinding;
    NestedScrollView galleryViewLayout;
    ImageView cropImageView;
    boolean isCropViewEnabled;
    ArrayList<LocalFile> listOfAllFiles;
    ArrayList<ArrayList<LocalFile>> listOfAllFilesByDir;
    ArrayList<String> listOfAllFoldersNames;
    ImageView closeBtn;
    RecyclerView localResView;
    FrameLayout imagePreviewLayout, videoPreviewLayout;
    ImageView nextBtn;
    ImageView chooseLocationIcon;
    TextView chooseLocationTitle;
    ImageView imagePreview;
    VideoView videoPreview;
    PickResourceScreen pickResourceScreen;
    ArrayList<LocalFile> listOfFilesInDevice;
    ArrayList<Uri> listOfFilesUri;
    LocalFilesViewAdapter localFilesViewAdapter;
    RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();

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
                imagePreviewLayout = pickResScreenBinding.imagePreviewLayout;
                videoPreviewLayout = pickResScreenBinding.videoPreviewLayout;
                galleryViewLayout = pickResScreenBinding.galleryViewLayout;
                cropImageView = pickResScreenBinding.zoomImageView;
                videoPreviewLayout.setVisibility(View.GONE);
                imagePreviewLayout.setVisibility(View.GONE);
                listOfFilesInDevice = new ArrayList<>();
                listOfFilesUri = new ArrayList<>();

                closeBtn.setOnClickListener(onClickClose -> {
                    try {
                        pickResourceScreen.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                isCropViewEnabled = false;
                cropImageView.setOnClickListener(onClickCropView ->{
                    if(!isCropViewEnabled){
                        imagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        isCropViewEnabled = true;
                        cropImageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.normal_view_ic));
                    }else{
                        imagePreview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        isCropViewEnabled = false;
                        cropImageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.full_view_ic));
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
                localFilesViewAdapter = new LocalFilesViewAdapter(requireContext(), listOfFilesInDevice);
                localResView.setAdapter(localFilesViewAdapter);
                localResView.setRecycledViewPool(recycledViewPool);
                localResView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                localFilesViewAdapter.setOnClickListenerOverride((position, localFile) -> {
                    if(localFile != null){
                        previewFile(localFile.getFile());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(()->{
                listOfAllFiles = FileHelper.combineMediaFiles(FileHelper.groupMediaFilesByDirectory(requireContext()));
                listOfFilesInDevice.addAll(listOfAllFiles);
                listOfFilesInDevice.addAll(listOfAllFiles);
                listOfFilesInDevice.addAll(listOfAllFiles);
                listOfFilesInDevice.addAll(listOfAllFiles);


                new Handler(Looper.getMainLooper()).post(()-> {
                    localFilesViewAdapter.notifyDataSetChanged();
                    LocalFile firstFile = listOfAllFiles.get(0);
                    if(firstFile != null){
                        previewFile(firstFile.getFile());
                    }
                });
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void previewFile(File file) {
        if(file != null){
            if(FileHelper.isVideoFile(file)){
                if(videoPreview.isPlaying()){
                    videoPreview.stopPlayback();
                }
                imagePreviewLayout.setVisibility(View.GONE);
                videoPreviewLayout.setVisibility(View.VISIBLE);
                videoPreview.setVideoPath(file.getAbsolutePath());
                videoPreview.start();
                videoPreview.setOnCompletionListener(mp -> {
                    mp.seekTo(0);
                    mp.start();
                });
                galleryViewLayout.smoothScrollTo(videoPreviewLayout.getScrollX(), videoPreviewLayout.getScrollY());
            }else{
                if(videoPreview.isPlaying()){
                    videoPreview.stopPlayback();
                }
                videoPreviewLayout.setVisibility(View.GONE);
                imagePreviewLayout.setVisibility(View.VISIBLE);
                RequestBuilder<Drawable> thumbnailRequest = Glide
                        .with(requireContext())
                        .load(file);
                Glide.with(requireContext())
                        .load(file)
                        .thumbnail(thumbnailRequest)
                        .into(imagePreview);
            galleryViewLayout.smoothScrollTo(imagePreviewLayout.getScrollX(), imagePreviewLayout.getScrollY());
            }
        }
    }

}