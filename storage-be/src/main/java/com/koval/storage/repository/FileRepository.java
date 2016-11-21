package com.koval.storage.repository;

import com.koval.storage.domain.Comment;
import com.koval.storage.domain.FileInfo;
import com.koval.storage.domain.FileSearchCommand;
import com.koval.storage.exception.EntityNotFoundException;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Created by Volodymyr Kovalenko
 */
@Repository
public class FileRepository {
    private static final String FIELD_COMMENTS = "comments";
    private static final String FIELD_EXTENSION = "extension";
    @Autowired
    private GridFsTemplate gridFsTemplate;

    public FileInfo save(InputStream inputStream, FileInfo fileInfo) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(FIELD_EXTENSION, fileInfo.getExtension());
        dbObject.put(FIELD_COMMENTS, new BasicDBList());

        GridFSFile gridFSFile = gridFsTemplate.store(inputStream, fileInfo.getName(), fileInfo.getContentType(), dbObject);
        fileInfo.setFileId(String.valueOf(gridFSFile.getId()));
        fileInfo.setLength(gridFSFile.getLength());
        fileInfo.setUploadTime(gridFSFile.getUploadDate().toInstant());

        return fileInfo;
    }

    public void deleteBefore(Instant before) {
        gridFsTemplate.delete(Query.query(Criteria.where("uploadDate").lte(before)));
    }

    public FileInfo findOne(String fileId) {
        return toDomain(getById(fileId));
    }

    private GridFSDBFile getById(String fileId) {
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        if (gridFSDBFile == null) {
            throw new EntityNotFoundException(fileId);
        }
        return gridFSDBFile;
    }

    public List<FileInfo> findAll(FileSearchCommand build) {
        Query query = new Query();
        ofNullable(build.getExtension()).filter(StringUtils::isNotBlank).ifPresent(ext -> query.addCriteria(GridFsCriteria.whereMetaData(FIELD_EXTENSION).is(ext)));
        ofNullable(build.getName()).filter(StringUtils::isNotBlank).ifPresent(name -> query.addCriteria(GridFsCriteria.whereFilename().regex(".*" + name + ".*")));
        addDateCriteria(query, build);

        return gridFsTemplate.find(query)
                .stream()
                .map(this::toDomain)
                .collect(toList());
    }

    public void addComment(String fileId, String comment) {
        GridFSDBFile file = getById(fileId);
        BasicDBList dbObject = (BasicDBList) file.getMetaData().get(FIELD_COMMENTS);
        dbObject.add(comment);
        file.save();
    }

    public List<Comment> getComments(String fileId) {
        GridFSDBFile file = getById(fileId);
        BasicDBList dbObject = (BasicDBList) file.getMetaData().get(FIELD_COMMENTS);
        return dbObject.stream().map(String::valueOf).map(initFunc(fileId)).collect(toList());
    }

    private Function<String, Comment> initFunc(String fileId) {
        return com -> {
            Comment comment = new Comment();
            comment.setText(com);
            comment.setFileId(fileId);
            return comment;
        };
    }

    private FileInfo toDomain(GridFSDBFile source) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setLength(source.getLength());
        fileInfo.setFileId(String.valueOf(source.getId()));
        fileInfo.setUploadTime(source.getUploadDate().toInstant());
        fileInfo.setName(source.getFilename());
        fileInfo.setContentType(source.getContentType());
        ofNullable(source.getMetaData())
                .map(t -> t.get("extension")).map(String::valueOf)
                .ifPresent(fileInfo::setExtension);
        return fileInfo;
    }

    private void addDateCriteria(Query query, FileSearchCommand build) {
        List<Consumer<Criteria>> dateCons = new ArrayList<>();
        ofNullable(build.getFrom()).ifPresent(from -> dateCons.add(crit -> crit.gte(from)));
        ofNullable(build.getTo()).ifPresent(to -> dateCons.add(crit -> crit.lte(to)));

        if (!dateCons.isEmpty()) {
            Criteria criteria = new Criteria("uploadDate");
            dateCons.stream().forEach(cons -> cons.accept(criteria));
            query.addCriteria(criteria);
        }
    }

    public InputStream getContent(String fileId) {
        return getById(fileId).getInputStream();
    }
}
