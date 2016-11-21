package com.koval.storage.domain;

import org.springframework.hateoas.ResourceSupport;

import java.util.Objects;

/**
 * Created by Volodymyr Kovalenko
 */
public class Comment extends ResourceSupport {
    private String fileId;
    private String text;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(fileId, comment.fileId) &&
                Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileId, text);
    }
}
