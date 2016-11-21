package com.koval.storage.service;

import com.koval.storage.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentServiceTest {
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    CommentService commentService = new CommentService();

    @Test
    public void testSaveAddComment() {
        commentService.addComment("fileId", "commentId");

        verify(fileRepository).addComment("fileId", "commentId");
    }

    @Test
    public void testGetComments() {
        commentService.find("fileId");

        verify(fileRepository).getComments("fileId");
    }
}
