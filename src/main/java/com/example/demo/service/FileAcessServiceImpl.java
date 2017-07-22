package com.example.demo.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dataapi.IFiledataAccess;
import com.example.demo.domain.FileData;
import com.example.demo.domain.FileMetaData;

@Service("fileAcessService")
public class FileAcessServiceImpl implements IFileAccessService,Serializable {
	

    private static final long serialVersionUID = 8119784722798361327L;
    
    @Autowired
    private IFiledataAccess fileDataAccess;

    /**
     * Saves a filedata.
     */
    @Override
    public FileMetaData save(FileData fileData) {
    	getFileDataAccess().insert(fileData); 
        return fileData.getMetadata();
    }
    
    /**
     * Finds file.
     */
    @Override
    public List<FileMetaData> findFiles(String personName, Date date) {
        return getFileDataAccess().findByUserNameDate(personName, date);
    }
    
    /**
     * ets the  file.
     */
    @Override
    public byte[] getFileId(String id) {
    	FileData fileData = getFileDataAccess().load(id);
        if(fileData!=null) {
            return fileData.getFileContent();
        } else {
            return null;
        }
    }


    public IFiledataAccess getFileDataAccess() {
        return fileDataAccess;
    }

    public void setFileDataAccess(IFiledataAccess fileDataAccess) {
    	fileDataAccess = fileDataAccess;
    }
}
