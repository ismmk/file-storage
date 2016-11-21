package com.koval.storage.web;

import com.koval.storage.domain.Comment;
import com.koval.storage.resource.CommentResource;
import com.koval.storage.resource.CommentResourceAssembler;
import com.koval.storage.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Volodymyr Kovalenko
 */
@Controller
@ExposesResourceFor(CommentResource.class)
@RequestMapping(path = "/api/files/{fileId}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentResourceAssembler commentResourceAssembler;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public HttpEntity<List<CommentResource>> list(@PathVariable("fileId") String fileId){
        List<Comment> fileInfo = commentService.find(fileId);
        return new HttpEntity<>(fileInfo
                .stream()
                .map(commentResourceAssembler::toResource)
                .collect(Collectors.toList()));
    }

    @RequestMapping(path = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void list(@PathVariable("fileId") String fileId,
                                                  @RequestBody String text){
        commentService.addComment(fileId, text);
    }
}
