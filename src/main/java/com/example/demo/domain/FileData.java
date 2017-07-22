package com.example.demo.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

public class FileData extends FileMetaData implements Serializable {

	 private byte[] fileContent;
	    
	    public FileData( byte[] fileContent, String fileName, Date fileUploadDate, String username) {
	        super(fileName, fileUploadDate, username);
	        this.fileContent = fileContent;
	    }

	    public FileData(Properties properties) {
	        super(properties);
	    }
	    
	    public FileData(FileMetaData metadata) {
	        super( metadata.getFileName(), metadata.getFileUploadDate(), metadata.getUsername());
	    }

	    public byte[] getFileContent() {
	        return fileContent;
	    }
	    public void setFileData(byte[] fileContent) {
	        this.fileContent = fileContent;
	    }
	    
	    public FileMetaData getMetadata() {
	        return new FileMetaData(getFileName(), getFileUploadDate(), getUsername());
	    }
}

