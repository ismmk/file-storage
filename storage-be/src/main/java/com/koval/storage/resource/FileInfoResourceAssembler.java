package com.koval.storage.resource;

import com.koval.storage.domain.Comment;
import com.koval.storage.domain.FileInfo;
import com.koval.storage.web.CommentController;
import com.koval.storage.web.FileInfoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Volodymyr Kovalenko
 */
@Component
public class FileInfoResourceAssembler extends ResourceAssemblerSupport<FileInfo, FileInfoResource> {
    @Autowired
    private EntityLinks entityLinks;

    public FileInfoResourceAssembler() {
        super(FileInfoController.class, FileInfoResource.class);
    }

    @Override
    public FileInfoResource toResource(FileInfo fileInfo) {
        FileInfoResource fileInfoResource = createResourceWithId(fileInfo.getFileId(), fileInfo);
        fileInfoResource.add(new Link(
                entityLinks.linkFor(CommentResource.class, fileInfo.getFileId()).toString() + "/")
                    .withRel("comments"));
        fileInfoResource.add(linkTo(FileInfoController.class).slash(fileInfo.getFileId()).slash("download")
                .withRel("download"));
        return fileInfoResource;
    }

    @Override
    protected FileInfoResource instantiateResource(FileInfo fileInfo) {
        FileInfoResource fileInfoResource = new FileInfoResource();

        fileInfoResource.extension = fileInfo.getExtension();
        fileInfoResource.fileId = fileInfo.getFileId();
        fileInfoResource.contentType = fileInfo.getContentType();
        fileInfoResource.name = fileInfo.getName();
        fileInfoResource.length = fileInfo.getLength();
        fileInfoResource.uploadTime = fileInfo.getUploadTime();
        return fileInfoResource;
    }
}
