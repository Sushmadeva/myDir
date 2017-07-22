package com.example.demo.dataapi;

import java.util.Date;
import java.util.List;

import com.example.demo.domain.FileData;
import com.example.demo.domain.FileMetaData;

public interface IFiledataAccess {
	 /**
     * Inserts a document in the data store.
     */
    void insert(FileData fileData);
    
    /**
     * Finds documents in the data store 
     */
    List<FileMetaData> findByUserNameDate(String personName, Date date);
    
    FileData load(String uuid);
}
