package com.koval.storage.domain;

import java.time.Instant;
import java.util.Objects;


/**
 * Created by Volodymyr Kovalenko
 */
public class FileInfo {
    private String fileId;
    private String name;
    private Long length;
    private String contentType;
    private Instant uploadTime;
    private String extension;

    public FileInfo() {
    }

    public FileInfo(String name, String contentType, String extension) {
        this.name = name;
        this.contentType = contentType;
        this.extension = extension;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileInfo fileInfo = (FileInfo) o;
        return Objects.equals(fileId, fileInfo.fileId) &&
                Objects.equals(name, fileInfo.name) &&
                Objects.equals(length, fileInfo.length) &&
                Objects.equals(contentType, fileInfo.contentType) &&
                Objects.equals(uploadTime, fileInfo.uploadTime) &&
                Objects.equals(extension, fileInfo.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, name, length, contentType, uploadTime, extension);
    }
}
