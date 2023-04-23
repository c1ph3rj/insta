package com.c1ph3rj.insta.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.c1ph3rj.insta.common.model.LocalFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileHelper {
    private static ArrayList<String> allTheMediaDirNames;

    public static ArrayList<String> getAllTheMediaDirNames() {
        return allTheMediaDirNames;
    }

    public static ArrayList<ArrayList<LocalFile>> groupMediaFilesByDirectory(Context context) {
        new Thread(() -> {
            // perform any background tasks here if needed
        }).start();

        ArrayList<ArrayList<LocalFile>> mediaFilesByDir = new ArrayList<>();
        allTheMediaDirNames = new ArrayList<>(); // new ArrayList to store all folder names

        String[] projection = new String[] {
                MediaStore.Images.Media.DATA,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Images.Media.DATE_MODIFIED, // add date modified to projection
                MediaStore.Video.Media.DATE_MODIFIED // add date modified to projection
        };

        String selection = MediaStore.Images.Media.MIME_TYPE + "=? OR " +
                MediaStore.Video.Media.MIME_TYPE + "=?";

        String[] selectionArgs = new String[] {"image/jpeg", "video/mp4"};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                selectionArgs,
                MediaStore.Images.Media.DATE_ADDED + " DESC");

        if (cursor != null) {
            HashMap<File, ArrayList<LocalFile>> tempMediaFilesByDir = new HashMap<>();

            while (cursor.moveToNext()) {
                String filePath = cursor.getString(0);
                File mediaFile = new File(filePath);

                String duration = cursor.getString(2);
                String fileName = cursor.getString(3);
                String fileType = cursor.getString(4);
                long lastModified = Math.max(cursor.getLong(5), cursor.getLong(6)); // get the maximum last modified value from both columns

                File parentDir = mediaFile.getParentFile();
                if (!tempMediaFilesByDir.containsKey(parentDir)) {
                    tempMediaFilesByDir.put(parentDir, new ArrayList<>());
                    assert parentDir != null;
                    allTheMediaDirNames.add(parentDir.getName()); // add folder name to new ArrayList
                }

                if(mediaFile.exists() && mediaFile.isFile()){
                    LocalFile localFile = new LocalFile(mediaFile, duration, fileName, fileType, lastModified);
                    Objects.requireNonNull(tempMediaFilesByDir.get(parentDir)).add(localFile);
                }
            }

            cursor.close();

            for (Map.Entry<File, ArrayList<LocalFile>> entry : tempMediaFilesByDir.entrySet()) {
                ArrayList<LocalFile> localFiles = entry.getValue();
                // sort the localFiles by last modified time in descending order (most recent first)
                localFiles.sort((o1, o2) -> Long.compare(o2.getLastModified(), o1.getLastModified()));
                mediaFilesByDir.add(localFiles);
            }
        }

        return mediaFilesByDir;
    }



    public static ArrayList<LocalFile> combineMediaFiles(ArrayList<ArrayList<LocalFile>> mediaFilesByDir) {
        ArrayList<LocalFile> combinedFiles = new ArrayList<>();

        for (ArrayList<LocalFile> files : mediaFilesByDir) {
            combinedFiles.addAll(files);
        }

        return combinedFiles;
    }

    public static ArrayList<ArrayList<LocalFile>> convertHashMapToList(HashMap<File, ArrayList<LocalFile>> hashMap) {
        ArrayList<ArrayList<LocalFile>> list = new ArrayList<>();

        for (Map.Entry<File, ArrayList<LocalFile>> entry : hashMap.entrySet()) {
            ArrayList<LocalFile> localFiles = entry.getValue();
            list.add(localFiles);
        }

        return list;
    }

    public static byte[] convertFileToByteArray(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        fis.close();
        return bos.toByteArray();
    }

    @SuppressLint("DefaultLocale")
    public static String getVideoDurationFormatted(Context context, File videoFile) {
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(videoFile));
            int durationMs = mediaPlayer.getDuration();
            mediaPlayer.release();

            int totalSeconds = durationMs / 1000;
            int hours = totalSeconds / 3600;
            int minutes = (totalSeconds % 3600) / 60;
            int seconds = totalSeconds % 60;

            if (hours > 0) {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            } else {
                return String.format("%02d:%02d", minutes, seconds);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isVideoFile(File selectedFile) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedFile.getName());
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        if (type != null) {
            return !type.contains("image");
        } else {
            return false;
        }
    }

}
