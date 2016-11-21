package com.koval.storage.test;

import com.koval.storage.domain.FileSearchCommand;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Created by Volodymyr Kovalenko
 */
public class SearchCommandBuilder{
    private String extension;
    private String name;
    private Instant from;
    private Instant to;

    public static SearchCommandBuilder builder(){
        return new SearchCommandBuilder();
    }

    public FileSearchCommand build(){
        FileSearchCommand fileSearchCommand = new FileSearchCommand();
        fileSearchCommand.setExtension(extension);
        fileSearchCommand.setName(name);
        fileSearchCommand.setFrom(from);
        fileSearchCommand.setTo(to);
        return fileSearchCommand;
    }

    public SearchCommandBuilder withExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public SearchCommandBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SearchCommandBuilder withFrom(Instant from) {
        this.from = from;
        return this;
    }

    public SearchCommandBuilder withTo(Instant to) {
        this.to = to;
        return this;
    }
}