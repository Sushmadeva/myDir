package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.domain.FileData;
import com.example.demo.domain.FileMetaData;

public interface IFileAccessService {

	/**
     * Saves a file in the archive.
     * 
     */
	FileMetaData save(FileData filedata);
    
    /**
     * Finds file in the archive matching the given parameter.
     *
     */
    List<FileMetaData> findFiles(String personName, Date date);
    
    
    /**
     * gets the  file from the archive with the given id.
     */
    byte[] getFileId(String id);
}
