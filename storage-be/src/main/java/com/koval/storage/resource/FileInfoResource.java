package com.koval.storage.resource;

import org.springframework.hateoas.ResourceSupport;

import java.time.Instant;

/**
 * Created by Volodymyr Kovalenko
 */
public class FileInfoResource extends ResourceSupport {
    String fileId;
    String name;
    Long length;
    String contentType;
    Instant uploadTime;
    String extension;

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
}
