package com.koval.storage.resource;

import com.koval.storage.domain.Comment;
import com.koval.storage.domain.FileInfo;
import com.koval.storage.web.CommentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by Volodymyr Kovalenko
 */
@Component
public class CommentResourceAssembler extends ResourceAssemblerSupport<Comment, CommentResource> {
    @Autowired
    private EntityLinks entityLinks;

    public CommentResourceAssembler() {
        super( CommentController.class, CommentResource.class);
    }

    @Override
    public CommentResource toResource(Comment entity) {
        CommentResource commentResource = new CommentResource();
        commentResource.add(entityLinks.linkToSingleResource(FileInfoResource.class, entity.getFileId()).withRel("file"));
        commentResource.fileId = entity.getFileId();
        commentResource.text = entity.getText();
        return commentResource;
    }
}
