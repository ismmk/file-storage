package com.koval.storage.web;

import com.koval.storage.Application;
import com.koval.storage.domain.FileInfo;
import com.koval.storage.domain.FileSearchCommand;
import com.koval.storage.repository.FileRepository;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.apache.commons.io.input.ReaderInputStream;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.Instant;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class FileInfoControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    private MockMvc mockMvc;
    private FileInfo fileInfo1;
    private FileInfo fileInfo2;

    @Before
    public void before() {
        gridFsTemplate.delete(new Query());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        fileInfo1 = new FileInfo("f11", "text/plain", "ext");
        fileInfo2 = new FileInfo("f22", "text/plain", "ext");
    }

    @Test
    public void testGetAllFiles() throws Exception {
        fileInfo1 = fileRepository.save(data(), fileInfo1);
        fileInfo2 = fileRepository.save(data(), fileInfo2);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/files/");

        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("*").value(hasSize(2)))
                .andExpect(jsonPath("*.fileId").value(containsInAnyOrder(fileInfo1.getFileId(), fileInfo2.getFileId())))
                .andExpect(jsonPath("*.name").value(containsInAnyOrder("f11", "f22")));
    }

    private InputStream data() {
        return new ByteInputStream("content".getBytes(), 7);
    }

    @Test
    public void testGetFilesSearch() throws Exception {
        fileInfo1 = fileRepository.save(data(), fileInfo1);
        fileInfo2 = fileRepository.save(data(), fileInfo2);
        fileRepository.save(data(), new FileInfo("f12", "jpg", "ext2"));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/files/?extension=ext&name=f1&to" + Instant.now().toEpochMilli());

        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath("*").value(hasSize(1)))
                .andExpect(jsonPath("*.fileId").value(containsInAnyOrder(fileInfo1.getFileId())))
                .andExpect(jsonPath("*.name").value(containsInAnyOrder("f11")))
                .andReturn().getResponse();
    }

    @Test
    public void testGet1File() throws Exception {
        fileInfo1 = fileRepository.save(data(), fileInfo1);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/api/files/" + fileInfo1.getFileId());

        mockMvc.perform(requestBuilder)
                .andExpect(jsonPath(".fileId").value(fileInfo1.getFileId()))
                .andExpect(jsonPath(".name").value("f11"))
                .andExpect(jsonPath(".extension").value("ext"))
                .andExpect(jsonPath(".contentType").value("text/plain"));
    }

    @Test
    public void testGetFileNotFound() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                get("/api/files/23");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile jsonFile = new MockMultipartFile("file", "file.js", "application/json", "{\"json\": \"someValue\"}".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/api/files/")
                .file(jsonFile))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath(".name").value("file.js"))
                .andExpect(jsonPath(".extension").value("js"))
                .andExpect(jsonPath(".contentType").value("application/json"));


        assertThat("file.js",
                isIn(fileRepository.findAll(new FileSearchCommand())
                        .stream()
                        .map(FileInfo::getName)
                        .collect(Collectors.toList())));
    }

    @Test
    public void testDownloadFile()  throws Exception{
        fileInfo1 = fileRepository.save(data(), fileInfo1);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/"  + fileInfo1.getFileId() + "/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=" + fileInfo1.getName()))
                .andExpect(content().string("content"))
                .andExpect(content().contentType("text/plain"))
                .andReturn();
    }
}
