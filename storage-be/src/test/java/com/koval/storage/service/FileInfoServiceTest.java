package com.koval.storage.service;

import com.koval.storage.domain.FileInfo;
import com.koval.storage.repository.FileRepository;
import com.koval.storage.service.FileInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Volodymyr Kovalenko
 */
@RunWith(MockitoJUnitRunner.class)
public class FileInfoServiceTest {
    public static final String FILE_NAME = "File1.jpg";
    public static final String CONTENT_TYPE = "type";
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    FileInfoService fileInfoService = new FileInfoService();
    private MultipartFile multipartFile;
    private InputStream inputStream;

    @Before
    public void before() throws Exception {
        multipartFile = mock(MultipartFile.class);
        inputStream = mock(InputStream.class);

        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(multipartFile.getContentType()).thenReturn(CONTENT_TYPE);
    }

    @Test
    public void testSaveShouldPersistCorrectValue() throws Exception {
        fileInfoService.save(multipartFile);
        ArgumentCaptor<FileInfo> infoArgumentCaptor = ArgumentCaptor.forClass(FileInfo.class);

        verify(fileRepository).save(eq(inputStream), infoArgumentCaptor.capture());

        FileInfo fileInfo1 = infoArgumentCaptor.getValue();
        assertEquals(FILE_NAME, fileInfo1.getName());
        assertEquals("jpg", fileInfo1.getExtension());
        assertEquals(CONTENT_TYPE, fileInfo1.getContentType());
    }
    @Test
    public void testSaveShouldPersistReturnValue() throws Exception {
        FileInfo fileInfo1 = new FileInfo();
        when(fileRepository.save(eq(inputStream), any(FileInfo.class))).thenReturn(fileInfo1);

        FileInfo fileInfo2 = fileInfoService.save(multipartFile);

        assertEquals(fileInfo1, fileInfo2);
    }
}
