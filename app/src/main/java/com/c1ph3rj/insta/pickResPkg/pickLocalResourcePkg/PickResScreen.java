package com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.c1ph3rj.insta.MainActivity.STORAGE_PERMISSION;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
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
import com.c1ph3rj.insta.utils.permissionsPkg.PermissionHandler;
import com.google.android.material.button.MaterialButton;

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
    FrameLayout previewLayout;
    boolean isMultipleSelectEnabled;
    ImageView closeBtn;
    RecyclerView localResView;
    FrameLayout imagePreviewLayout, videoPreviewLayout;
    ImageView nextBtn;
    LinearLayout chooseLocationView;
    TextView chooseLocationTitle;
    ImageView imagePreview;
    VideoView videoPreview;
    MaterialButton multipleSelectBtn;
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
                chooseLocationTitle = pickResScreenBinding.chooseLocationTitle;
                chooseLocationView = pickResScreenBinding.chooseLocationView;
                localResView = pickResScreenBinding.listOfResourcesView;
                imagePreview = pickResScreenBinding.previewImagesView;
                videoPreview = pickResScreenBinding.previewVideoView;
                imagePreviewLayout = pickResScreenBinding.imagePreviewLayout;
                videoPreviewLayout = pickResScreenBinding.videoPreviewLayout;
                galleryViewLayout = pickResScreenBinding.galleryViewLayout;
                cropImageView = pickResScreenBinding.zoomImageView;
                previewLayout = pickResScreenBinding.previewLayout;
                multipleSelectBtn = pickResScreenBinding.multipleSelectFeature;
                videoPreviewLayout.setVisibility(View.GONE);
                imagePreviewLayout.setVisibility(View.GONE);
                listOfFilesInDevice = new ArrayList<>();
                listOfFilesUri = new ArrayList<>();

                chooseLocationTitle.setText("All Files");

                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;
                int screenHeight = (screenWidth * 2) / 3;
                ViewGroup.LayoutParams layoutParams = previewLayout.getLayoutParams();
                layoutParams.height = screenHeight;
                previewLayout.setLayoutParams(layoutParams);

                closeBtn.setOnClickListener(onClickClose -> {
                    try {
                        pickResourceScreen.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                isCropViewEnabled = false;
                cropImageView.setOnClickListener(onClickCropView -> {
                    if (!isCropViewEnabled) {
                        imagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        isCropViewEnabled = true;
                        cropImageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.normal_view_ic));
                    } else {
                        imagePreview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        isCropViewEnabled = false;
                        cropImageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.full_view_ic));
                    }
                });

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
                    if (localFile != null) {
                        previewFile(localFile.getFile());
                    }
                });
                multipleSelectBtn.setOnClickListener(onClickMultipleSelect ->{
                    if(isMultipleSelectEnabled){
                        multipleSelectBtn.setBackgroundColor(requireContext().getColor(R.color.lightGreyText));
                    }else{
                        multipleSelectBtn.setBackgroundColor(requireContext().getColor(R.color.instagramBlue));
                    }
                    localFilesViewAdapter.setSelectedPositionsList();
                    isMultipleSelectEnabled = !isMultipleSelectEnabled;
                    localFilesViewAdapter.setMultipleSelectEnabled(isMultipleSelectEnabled);
                    localFilesViewAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                listOfAllFilesByDir = FileHelper.groupMediaFilesByDirectory(requireContext());
                listOfAllFiles = FileHelper.combineMediaFiles(listOfAllFilesByDir);
                listOfFilesInDevice.addAll(listOfAllFiles);

                new Handler(Looper.getMainLooper()).post(() -> {
                    localFilesViewAdapter.notifyDataSetChanged();
                    LocalFile firstFile = listOfAllFiles.get(0);
                    if (firstFile != null) {
                        previewFile(firstFile.getFile());
                    }
                });
            }).start();

            chooseLocationView.setOnClickListener(onClickChooseLocation ->{
                Dialog dialog = new Dialog(requireContext());
                LayoutInflater inflater = (LayoutInflater)
                        requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.resource_location_layout, null);
                dialog.setContentView(popupView);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireActivity(), R.drawable.transparent_bg));

                ListView listOfResourceNamesView = dialog.findViewById(R.id.listOfResourcesLocationView);
                listOfAllFoldersNames = new ArrayList<>();
                listOfAllFoldersNames.add("All Files");
                listOfAllFoldersNames.addAll(FileHelper.getAllTheMediaDirNames());
                ArrayAdapter<String> listOfAllResNamesAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, listOfAllFoldersNames);
                listOfResourceNamesView.setAdapter(listOfAllResNamesAdapter);
                listOfResourceNamesView.setOnItemClickListener((parent, view, position, id) -> {
                    chooseLocationTitle.setText(listOfAllFoldersNames.get(position));
                    localFilesViewAdapter.setSelectedPositionsList();
                    if(listOfAllFoldersNames.get(position).equals("All Files")){
                        listOfFilesInDevice.clear();
                        listOfFilesInDevice.addAll(listOfAllFiles);
                        localFilesViewAdapter.notifyDataSetChanged();
                    }else{
                        listOfFilesInDevice.clear();
                        listOfFilesInDevice.addAll(listOfAllFilesByDir.get(position - 1));
                        localFilesViewAdapter.notifyDataSetChanged();
                    }
                    previewFile(listOfFilesInDevice.get(0).getFile());
                    dialog.dismiss();
                });

                dialog.show();
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void previewFile(File file) {
        if (file != null) {
            if (FileHelper.isVideoFile(file)) {
                if (videoPreview.isPlaying()) {
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
            } else {
                if (videoPreview.isPlaying()) {
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