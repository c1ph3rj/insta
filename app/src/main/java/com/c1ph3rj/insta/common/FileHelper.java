package com.c1ph3rj.insta.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.c1ph3rj.insta.common.model.LocalFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileHelper {

    public static ArrayList<ArrayList<LocalFile>> groupMediaFilesByDirectory(Context context) {
        ArrayList<ArrayList<LocalFile>> mediaFilesByDir = new ArrayList<>();

        String[] projection = new String[] {
                MediaStore.Images.Media.DATA,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Files.FileColumns.MIME_TYPE
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

                if (!tempMediaFilesByDir.containsKey(mediaFile.getParentFile())) {
                    tempMediaFilesByDir.put(mediaFile.getParentFile(), new ArrayList<>());
                }

                LocalFile localFile = new LocalFile(mediaFile, duration, fileName, fileType);
                tempMediaFilesByDir.get(mediaFile.getParentFile()).add(localFile);
            }

            cursor.close();

            for (Map.Entry<File, ArrayList<LocalFile>> entry : tempMediaFilesByDir.entrySet()) {
                ArrayList<LocalFile> localFiles = entry.getValue();
                mediaFilesByDir.add(localFiles);
            }
        }

        return mediaFilesByDir;
    }


    public static HashMap<File, List<File>> groupMediaFilesByDirectoryf(Context context) {
        HashMap<File, List<File>> mediaFilesByDir = new HashMap<>();

        String[] projection = new String[] {
                MediaStore.Images.Media.DATA,
                MediaStore.Video.Media.DATA
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
            while (cursor.moveToNext()) {
                String filePath = cursor.getString(0);
                File mediaFile = new File(filePath);

                File parentDir = mediaFile.getParentFile();

                if (!mediaFilesByDir.containsKey(parentDir)) {
                    mediaFilesByDir.put(parentDir, new ArrayList<>());
                }

                Objects.requireNonNull(mediaFilesByDir.get(parentDir)).add(mediaFile);
            }
            cursor.close();
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

    public static Bitmap createVideoThumbnail(File videoFile) throws IOException {
        Bitmap thumbnail = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            // Set the data source for the MediaMetadataRetriever to the video file
            retriever.setDataSource(videoFile.getAbsolutePath());

            // Get the thumbnail of the video at the first frame
            thumbnail = retriever.getFrameAtTime(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Release the MediaMetadataRetriever
            retriever.release();
        }
        return thumbnail;
    }
}
