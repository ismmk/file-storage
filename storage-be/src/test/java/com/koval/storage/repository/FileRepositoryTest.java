package com.koval.storage.repository;

import com.koval.storage.Application;
import com.koval.storage.domain.Comment;
import com.koval.storage.domain.FileInfo;
import com.koval.storage.domain.FileSearchCommand;
import com.koval.storage.test.SearchCommandBuilder;
import com.mongodb.gridfs.GridFSDBFile;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public class FileRepositoryTest {
    public static final ByteInputStream STREAM = new ByteInputStream(new byte[]{1, 2}, 2);
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    private FileInfo fileInfo1;
    private FileInfo fileInfo2;

    @Before
    public void before() {
        gridFsTemplate.delete(new Query());

        fileInfo1 = new FileInfo("f1", "jpg", "ext");
        fileInfo2 = new FileInfo("f2", "jpg", "ext");
    }

    @Test
    public void testCreate() {
        FileInfo fileInfo = fileRepository.save(STREAM, fileInfo1);

        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileInfo.getFileId())));

        assertEquals("f1", gridFSDBFile.getFilename());
        assertEquals("ext", gridFSDBFile.getMetaData().get("extension"));
        assertEquals("jpg", gridFSDBFile.getContentType());
    }

    @Test
    public void testFind() {
        FileInfo fileInfo = fileRepository.save(STREAM, fileInfo1);

        FileInfo fileInfo2 = fileRepository.findOne(fileInfo.getFileId());

        assertEquals("f1", fileInfo2.getName());
        assertEquals("ext", fileInfo2.getExtension());
        assertEquals("jpg", fileInfo2.getContentType());
    }

    @Test
    public void testAddComments() {
        FileInfo fileInfo = fileRepository.save(STREAM, fileInfo1);
        fileRepository.addComment(fileInfo.getFileId(), "f1");
        fileRepository.addComment(fileInfo.getFileId(), "f2");

        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileInfo.getFileId())));

        List<String> stringList = (List<String>) gridFSDBFile.getMetaData().get("comments");

        assertThat(stringList, contains("f1", "f2"));
    }

    @Test
    public void testGetComments() {
        FileInfo fileInfo = fileRepository.save(STREAM, fileInfo1);
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileInfo.getFileId())));
        List<String> stringList = (List<String>) gridFSDBFile.getMetaData().get("comments");
        stringList.add("f1");
        stringList.add("f2");
        gridFSDBFile.save();

        List<Comment> comments = fileRepository.getComments(fileInfo.getFileId());
        assertEquals(2, comments.size());
        assertEquals("f1", comments.get(0).getText());
        assertEquals("f2", comments.get(1).getText());
    }

    @Test
    public void testDelete() throws Exception {
        FileInfo fileInfo11 = fileRepository.save(STREAM, fileInfo1);
        Thread.sleep(500);
        FileInfo fileInfo12 = fileRepository.save(STREAM, fileInfo2);

        fileRepository.deleteBefore(fileInfo12.getUploadTime().minus(250, ChronoUnit.MILLIS));

        assertEquals(0, gridFsTemplate.find(Query.query(Criteria.where("_id").is(fileInfo11.getFileId()))).size());
        assertEquals(1, gridFsTemplate.find(Query.query(Criteria.where("_id").is(fileInfo12.getFileId()))).size());
    }


    @Test
    public void testSearch() throws Exception {
        FileInfo fileInfo11 = fileRepository.save(STREAM, new FileInfo("name11", "type", "jpg1"));
        FileInfo fileInfo12 = fileRepository.save(STREAM, new FileInfo("name21", "type", "jpg2"));
        Thread.sleep(500);
        FileInfo fileInfo21 = fileRepository.save(STREAM, new FileInfo("name12", "type", "jpg1"));
        FileInfo fileInfo22 = fileRepository.save(STREAM, new FileInfo("name22", "type", "jpg2"));
        Thread.sleep(500);
        FileInfo fileInfo31 = fileRepository.save(STREAM, new FileInfo("name31", "type", "jpg1"));
        FileInfo fileInfo32 = fileRepository.save(STREAM, new FileInfo("name32", "type", "jpg2"));

        SearchCommandBuilder searchCommandBuilder = SearchCommandBuilder.builder();

        assertResult(searchCommandBuilder, fileInfo11, fileInfo12, fileInfo21, fileInfo22, fileInfo31, fileInfo32);
        searchCommandBuilder.withExtension("jpg1");
        assertResult(searchCommandBuilder, fileInfo11, fileInfo21, fileInfo31);
        searchCommandBuilder.withFrom(fileInfo21.getUploadTime().minus(200, ChronoUnit.MILLIS));
        assertResult(searchCommandBuilder, fileInfo21, fileInfo31);
        searchCommandBuilder.withTo(fileInfo31.getUploadTime().minus(200, ChronoUnit.MILLIS));
        assertResult(searchCommandBuilder, fileInfo21);
        searchCommandBuilder.withName("2");
        assertResult(searchCommandBuilder, fileInfo21);
        searchCommandBuilder.withName("name22");
        assertResult(searchCommandBuilder);
    }

    private void assertResult(SearchCommandBuilder searchCommandBuilder, FileInfo... files) {
        List<FileInfo> ft = fileRepository.findAll(searchCommandBuilder.build());

        assertThat(ft, containsInAnyOrder(files));
    }
}
