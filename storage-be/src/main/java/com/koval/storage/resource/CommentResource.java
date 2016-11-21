package com.koval.storage.resource;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Volodymyr Kovalenko
 */
public class CommentResource extends ResourceSupport {
    String fileId;
    String text;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
