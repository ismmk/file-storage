package com.koval.storage.web;

import com.koval.storage.domain.FileInfo;
import com.koval.storage.domain.FileSearchCommand;
import com.koval.storage.resource.FileInfoResource;
import com.koval.storage.resource.FileInfoResourceAssembler;
import com.koval.storage.service.FileInfoService;
import com.koval.storage.exception.StorageException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * Created by Volodymyr Kovalenko
 */
@RestController
@ExposesResourceFor(FileInfoResource.class)
@RequestMapping(path = "/api/files")
public class FileInfoController {

    @Autowired
    private FileInfoService fileService;
    @Autowired
    private FileInfoResourceAssembler fileInfoResourceAssembler;

    @RequestMapping(path = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<FileInfoResource> create(@RequestParam("file") MultipartFile file) {
        return new HttpEntity<>(fileInfoResourceAssembler.toResource(fileService.save(file)));
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public HttpEntity<List<FileInfoResource>> list(FileSearchCommand fileSearchCommand) {
        return new HttpEntity<>(fileService.find(fileSearchCommand)
                .stream()
                .map(fileInfoResourceAssembler::toResource)
                .collect(Collectors.toList()));
    }

    @RequestMapping(path = "/{fileId}", method = RequestMethod.GET)
    public HttpEntity<FileInfoResource> get(@PathVariable("fileId") String fileId) {
        return new HttpEntity<>(fileInfoResourceAssembler.toResource(fileService.get(fileId)));
    }

    @RequestMapping(path = "/{fileId}/download", method = RequestMethod.GET)
    public void download(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        FileInfo fileInfo = fileService.get(fileId);
        try {
            response.setContentType(fileInfo.getContentType());
            response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + fileInfo.getName());
            IOUtils.copy(fileService.getContent(fileId), response.getOutputStream());
        } catch (Exception e) {
            throw new StorageException();
        }

    }
}
