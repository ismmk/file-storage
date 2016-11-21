package com.koval.storage.service;

import com.koval.storage.domain.Comment;
import com.koval.storage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Volodymyr Kovalenko
 */
@Service
public class CommentService {
    @Autowired
    private FileRepository fileRepository;

    public List<Comment> find(String fileId) {
        return fileRepository.getComments(fileId);
    }

    public void addComment(String fileId, String commentId) {
        fileRepository.addComment(fileId, commentId);
    }
}
