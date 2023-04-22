package com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.common.FileHelper;
import com.c1ph3rj.insta.common.model.LocalFile;

import java.io.File;
import java.util.ArrayList;

public class LocalFilesViewAdapter extends RecyclerView.Adapter<LocalFilesViewAdapter.ViewHolder> {
    ArrayList<LocalFile> listOfFiles;
    Context context;

    public LocalFilesViewAdapter(Context context, ArrayList<LocalFile> listOfFiles) {
        this.context = context;
        this.listOfFiles = listOfFiles;
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
        File currentFile = listOfFiles.get(position).getFile();
        holder.photoItemView.setVisibility(View.GONE);
        holder.videoItemLayout.setVisibility(View.GONE);
        if (currentFile != null) {
            int fileType = getMediaType(currentFile);

            if (fileType == 0) {
                holder.videoItemLayout.setVisibility(View.GONE);
                holder.photoItemView.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(currentFile)
                        .skipMemoryCache(false)
                        .into(holder.photoItemView);
            } else if (fileType == 1) {
                holder.videoItemLayout.setVisibility(View.VISIBLE);
                holder.photoItemView.setVisibility(View.GONE);

                holder.videoDuration.setText(FileHelper.getVideoDurationFormatted(context, currentFile));

                Glide.with(context)
                        .load(currentFile)
                        .skipMemoryCache(false)
                        .into(holder.videoItemView);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listOfFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photoItemView;
        ImageView videoItemView;
        FrameLayout videoItemLayout;
        TextView videoDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoItemLayout = itemView.findViewById(R.id.videoItemLayout);
            photoItemView = itemView.findViewById(R.id.photoItemView);
            videoItemView = itemView.findViewById(R.id.videoItemView);
            videoDuration = itemView.findViewById(R.id.durationOfTheVideo);
        }
    }

    int getMediaType(File selectedFile) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedFile.getName());
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        if(type != null){
            if (type.contains("image"))
                return 0;
            else
                return 1;
        }else{
            return -1;
        }
    }
}
