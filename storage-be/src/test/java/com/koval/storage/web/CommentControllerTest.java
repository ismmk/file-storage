package com.koval.storage.web;

import com.koval.storage.Application;
import com.koval.storage.domain.Comment;
import com.koval.storage.domain.FileInfo;
import com.koval.storage.repository.FileRepository;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class CommentControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    private MockMvc mockMvc;
    private FileInfo fileInfo1;

    @Before
    public void before() {
        gridFsTemplate.delete(new Query());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        fileInfo1 = new FileInfo();
        fileInfo1.setName("f1");
        fileInfo1.setExtension("ext");
        fileInfo1.setContentType("jpg");

        fileInfo1 = fileRepository.save(new ByteInputStream(new byte[]{1, 2, 3}, 3), fileInfo1);
    }

    @Test
    public void testGetAllComments() throws Exception {
        fileRepository.addComment(fileInfo1.getFileId(), "f1");
        fileRepository.addComment(fileInfo1.getFileId(), "f2");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/api/files/" + fileInfo1.getFileId() + "/comments/");


        String ret = mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("*").value(hasSize(2)))
                .andExpect(jsonPath("*.fileId").value(containsInAnyOrder(fileInfo1.getFileId(), fileInfo1.getFileId())))
                .andExpect(jsonPath("*.text").value(containsInAnyOrder("f1", "f2")))
                .andReturn().getResponse().getContentAsString();
        System.out.println(ret);
    }
    @Test
    public void testSaveComment() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/api/files/" + fileInfo1.getFileId() + "/comments/").content("f1"));

        List<Comment> commentList = fileRepository.getComments(fileInfo1.getFileId());
        assertEquals(1, commentList.size());
        assertEquals("f1", commentList.get(0).getText());
    }

}
