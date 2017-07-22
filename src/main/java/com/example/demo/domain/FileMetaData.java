package com.example.demo.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;


public class FileMetaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String uuid;
	private String fileName;
	private String username;
	private Date fileUploadDate;

	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);


	private static final Logger LOG = Logger.getLogger(FileMetaData.class);
	public static final String FILE_UUID = "uuid";
	public static final String USER_NAME = "person-name";
	public static final String FILE_NAME = "file-name";
	public static final String FILE_UPLOAD_DATE = "file-date";

	public FileMetaData() {

	}

	 public FileMetaData(String fileName, Date documentDate, String personName) {
	        this(UUID.randomUUID().toString(), fileName, documentDate,personName);
	    }
	public FileMetaData( String uuid,String fileName, Date fileUploadDate, String username) {
		super();
		this.uuid = uuid;
		this.fileName = fileName;
		this.fileUploadDate = fileUploadDate;
		this.username = username;
	}


	public FileMetaData(Properties properties) {
		this(properties.getProperty(FILE_UUID),properties.getProperty(FILE_NAME),
				null,
				properties.getProperty(USER_NAME));
		String dateString = properties.getProperty(FILE_UPLOAD_DATE);
		if(dateString!=null) {
			try {
				this.fileUploadDate = DATE_FORMAT.parse(dateString);
			} catch (ParseException e) {
				LOG.error("Error while parsing date string: " + dateString + ", format is: yyyy-MM-dd" , e);
			}
		}    
	}
	public Properties createProperties() {
		Properties props = new Properties();
		 props.setProperty(FILE_UUID, getUuid());
		props.setProperty(FILE_NAME, getFileName());
		props.setProperty(USER_NAME, getUsername());
		props.setProperty(FILE_UPLOAD_DATE, DATE_FORMAT.format(getFileUploadDate()));
		return props;
	}
	
	  public String getUuid() {
	        return uuid;
	    }
	    public void setUuid(String uuid) {
	        this.uuid = uuid;
	    }

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getFileUploadDate() {
		return fileUploadDate;
	}
	public void setFileUploadDate(Date fileUploadDate) {
		this.fileUploadDate = fileUploadDate;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
