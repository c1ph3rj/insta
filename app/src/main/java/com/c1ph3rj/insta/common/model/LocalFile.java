package com.c1ph3rj.insta.common.model;

import java.io.File;

public class LocalFile {
    File file;
    String duration;
    String fileName;
    String fileType;
    long lastModified;

    public LocalFile(File file, String duration, String fileName, String fileType, long lastModified) {
        this.file = file;
        this.duration = duration;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
