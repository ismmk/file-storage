package com.koval.storage.service;

import com.koval.storage.domain.FileInfo;
import com.koval.storage.domain.FileSearchCommand;
import com.koval.storage.exception.StorageException;
import com.koval.storage.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Volodymyr Kovalenko
 */
@Service
public class FileInfoService {
    @Autowired
    private FileRepository fileRepository;

    public FileInfo save(MultipartFile file) {
           FileInfo fileInfo = new FileInfo();
        fileInfo.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1));
        fileInfo.setContentType( file.getContentType() );
        fileInfo.setName(file.getOriginalFilename());
        try {
            return fileRepository.save(file.getInputStream(), fileInfo);
        } catch (IOException e) {
            throw new StorageException();
        }
    }

    public List<FileInfo> find(FileSearchCommand fileSearchCommand) {
        return fileRepository.findAll(fileSearchCommand);
    }

    public FileInfo get(String fileId) {
        return fileRepository.findOne(fileId);
    }

    public InputStream getContent(String fileId) {
        return fileRepository.getContent(fileId);
    }
}
