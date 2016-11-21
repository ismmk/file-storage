package com.koval.storage.domain;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by Volodymyr Kovalenko
 */
public class FileSearchCommand {
    private String extension;
    private String name;
    private Instant from;
    private Instant to;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }
}
