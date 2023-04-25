package com.c1ph3rj.insta.pickResPkg.pickLocalResourcePkg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.c1ph3rj.insta.R;
import com.c1ph3rj.insta.common.FileHelper;
import com.c1ph3rj.insta.common.model.LocalFile;
import com.c1ph3rj.insta.utils.glideSupportPkg.GlideApp;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

public class LocalFilesViewAdapter extends RecyclerView.Adapter<LocalFilesViewAdapter.ViewHolder> {
    private final ArrayList<Integer> selectedPositionsList;
    ArrayList<LocalFile> listOfFiles;
    Context context;
    OnClickListener onClickListener;
    private boolean isMultipleSelectEnabled;

    public LocalFilesViewAdapter(Context context, ArrayList<LocalFile> listOfFiles) {
        this.context = context;
        this.listOfFiles = listOfFiles;
        selectedPositionsList = new ArrayList<>();
    }

    public ArrayList<Integer> getSelectedPositionsList() {
        return selectedPositionsList;
    }

    public void setSelectedPositionsList() {
        try {
            for (int selectPos : selectedPositionsList) {
                try {
                    listOfFiles.get(selectPos).setSelected(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.selectedPositionsList.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_files_view_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File currentFile = listOfFiles.get(position).getFile();
        LocalFile currentLocalFile = listOfFiles.get(position);
        holder.photoItemView.setVisibility(View.GONE);
        holder.videoItemLayout.setVisibility(View.GONE);
        if (isMultipleSelectEnabled) {
            if (currentLocalFile.isSelected()) {
                holder.selectedLayout.setBackgroundColor(context.getColor(R.color.instagramLightWhite));
                holder.selectBtn.setText(String.valueOf(selectedPositionsList.indexOf(position) + 1));
                holder.selectBtn.setBackgroundColor(context.getColor(R.color.instagramBlue));
            } else {
                holder.selectBtn.setText("");
                holder.selectedLayout.setBackgroundColor(context.getColor(android.R.color.transparent));
                holder.selectBtn.setBackgroundColor(context.getColor(R.color.lightGreyText));
            }
            holder.selectBtn.setOnClickListener(onClickSelectItem -> {
                if (currentLocalFile.isSelected()) {
                    selectedPositionsList.remove((Integer) position);
                    currentLocalFile.setSelected(false);
                    this.notifyDataSetChanged();
                } else {
                    if (selectedPositionsList.size() < 10) {
                        selectedPositionsList.add(position);
                        currentLocalFile.setSelected(true);
                        this.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "The limit is 10 photos or videos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.selectedLayout.setOnClickListener(onClickSelectLayout -> holder.selectBtn.performClick());
            holder.selectedLayout.setVisibility(View.VISIBLE);
        } else {
            holder.selectedLayout.setVisibility(View.GONE);
        }
        if (currentFile != null) {
            int fileType = getMediaType(currentFile);

            if (fileType == 0) {
                holder.videoItemLayout.setVisibility(View.GONE);
                holder.photoItemView.setVisibility(View.VISIBLE);

                RequestBuilder<Drawable> thumbnailRequest = GlideApp
                        .with(context)
                        .load(currentFile);
                Glide.with(context)
                        .load(currentFile)
                        .thumbnail(thumbnailRequest)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(holder.photoItemView);


            } else if (fileType == 1) {
                holder.videoItemLayout.setVisibility(View.VISIBLE);
                holder.photoItemView.setVisibility(View.GONE);
                holder.videoDuration.setText(FileHelper.getVideoDurationFormatted(context, currentFile));

                Glide.with(context)
                        .load(currentFile)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .into(holder.videoItemView);
            }
            holder.itemView.setOnClickListener(onClickMedia -> {
                if (onClickListener != null) {
                    onClickListener.onClick(position, listOfFiles.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listOfFiles.size();
    }

    int getMediaType(File selectedFile) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedFile.getName());
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        if (type != null) {
            if (type.contains("image"))
                return 0;
            else
                return 1;
        } else {
            return -1;
        }
    }

    public void setOnClickListenerOverride(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public boolean isMultipleSelectEnabled() {
        return isMultipleSelectEnabled;
    }

    public void setMultipleSelectEnabled(boolean multipleSelectEnabled) {
        isMultipleSelectEnabled = multipleSelectEnabled;
    }

    public interface OnClickListener {
        void onClick(int position, LocalFile localFile);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photoItemView;
        ImageView videoItemView;
        FrameLayout videoItemLayout;
        TextView videoDuration;
        LinearLayout selectedLayout;
        MaterialButton selectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoItemLayout = itemView.findViewById(R.id.videoItemLayout);
            photoItemView = itemView.findViewById(R.id.photoItemView);
            videoItemView = itemView.findViewById(R.id.videoItemView);
            videoDuration = itemView.findViewById(R.id.durationOfTheVideo);
            selectedLayout = itemView.findViewById(R.id.selectedLayout);
            selectBtn = itemView.findViewById(R.id.selectBtn);
        }
    }
}
