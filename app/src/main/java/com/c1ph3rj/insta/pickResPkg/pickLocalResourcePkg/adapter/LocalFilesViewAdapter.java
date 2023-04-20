package com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.c1ph3rj.insta.R;

import java.io.File;
import java.util.ArrayList;

public class LocalFilesViewAdapter extends RecyclerView.Adapter<LocalFilesViewAdapter.ViewHolder> {
    ArrayList<File> listOfFiles;
    ArrayList<Uri> listOfFilesUri;
    Context context;

    public LocalFilesViewAdapter(Context context, ArrayList<File> listOfFiles, ArrayList<Uri> listOfFilesUri) {
        this.context = context;

        this.listOfFiles = listOfFiles;
        this.listOfFilesUri = listOfFilesUri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_files_view_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File currentFile = listOfFiles.get(position);
        holder.photoItemView.setVisibility(View.GONE);
        holder.videoItemLayout.setVisibility(View.GONE);
        if (currentFile != null) {
            int fileType = getMediaType(listOfFilesUri.get(position));
            if (fileType == 0) {
                holder.videoItemLayout.setVisibility(View.GONE);
                holder.photoItemView.setVisibility(View.VISIBLE);
                try {
                    Glide.with(context)
                            .load(currentFile)
                            .into(holder.photoItemView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (fileType == 1) {
                holder.videoItemLayout.setVisibility(View.VISIBLE);
                holder.photoItemView.setVisibility(View.GONE);

//                holder.videoItemView.setImageBitmap(createVideoThumbNail(currentFile.getAbsolutePath()));
            }
        }
    }

    public Bitmap createVideoThumbNail(String path) {
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }

    @Override
    public int getItemCount() {
        return listOfFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photoItemView;
        ImageView videoItemView;
        FrameLayout videoItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoItemLayout = itemView.findViewById(R.id.videoItemLayout);
            photoItemView = itemView.findViewById(R.id.photoItemView);
            videoItemView = itemView.findViewById(R.id.videoItemView);
        }
    }

    int getMediaType(Uri uri) {
        String fileType = context.getContentResolver().getType(uri);
        if (fileType.startsWith("image")) {
            return 0;
        } else if (fileType.startsWith("video")) {
            return 1;
        } else {
            return 2;
        }
    }
}
