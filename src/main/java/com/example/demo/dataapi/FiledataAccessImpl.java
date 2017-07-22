package com.example.demo.dataapi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.example.demo.domain.FileData;
import com.example.demo.domain.FileMetaData;
@Service("filedataAccess")
public class FiledataAccessImpl implements IFiledataAccess{
	
	
private static final Logger LOG = Logger.getLogger(FiledataAccessImpl.class);
    
    public static final String DIRECTORY = "archive";
    public static final String META_DATA_FILE_NAME = "metadata.properties";
    
    @PostConstruct
    public void init() {
        createDirectory(DIRECTORY);
    }
    
    /**
     * Inserts a document to the archive 
     * @see org.murygin.archive.dao.IDocumentDao#insert(org.murygin.archive.service.Document)
     */
    @Override
    public void insert(FileData fileData) {
        try {
            createDirectory(fileData);
            saveFileData(fileData);
            saveMetaData(fileData);
        } catch (IOException e) {
            String message = "Error while inserting document";
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    /**
     * Finds documents in the data store 
     */
    @Override
    public List<FileMetaData> findByUserNameDate(String userName, Date date) {
        try {
            return findInFileSystem(userName,date);
        } catch (IOException e) {
            String message = "Error while finding document, user name: " + userName + ", date:" + date;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
    
    
    private List<FileMetaData> findInFileSystem(String userName, Date date) throws IOException  {
        List<String> uuidList = getUuidList();
        List<FileMetaData> metadataList = new ArrayList<FileMetaData>(uuidList.size());
        for (String uuid : uuidList) {
        	FileMetaData metadata = loadMetadataFromFileSystem(uuid);         
            if(isMatched(metadata, userName, date)) {
                metadataList.add(metadata);
            }
        }
        return metadataList;
    }

    private boolean isMatched(FileMetaData metadata, String username, Date date) {
        if(metadata==null) {
            return false;
        }
        boolean match = true;
        if(username!=null) {
            match = (username.equals(metadata.getUsername()));
        }
        if(match && date!=null) {
            match = (date.equals(metadata.getFileUploadDate()));
        }
        return match;
    }

    private FileMetaData loadMetadataFromFileSystem(String uuid) throws IOException {
    	FileMetaData metadata = null;
        String dirPath = getDirectoryPath(uuid);
        File file = new File(dirPath);
        if(file.exists()) {
            Properties properties = readProperties(uuid);
            metadata = new FileMetaData(properties);
            
        } 
        return metadata;
    }
    
    private FileData loadFromFileSystem(String uuid) throws IOException {
    	FileMetaData metadata = loadMetadataFromFileSystem(uuid);
       if(metadata==null) {
           return null;
       }
       Path path = Paths.get(getFilePath(metadata));
       FileData fileData = new FileData(metadata);
       fileData.setFileData(Files.readAllBytes(path));
       return fileData;
    }

    private String getFilePath(FileMetaData metadata) {
        String dirPath = getDirectoryPath(metadata.getUuid());
        StringBuilder sb = new StringBuilder();
        sb.append(dirPath).append(File.separator).append(metadata.getFileName());
        return sb.toString();
    }
    
    private void saveFileData(FileData fileData) throws IOException {
        String path = getDirectoryPath(fileData);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), fileData.getFileName())));
        stream.write(fileData.getFileContent());
        stream.close();
    }
    
    public void saveMetaData(FileData fileData) throws IOException {
            String path = getDirectoryPath(fileData);
            Properties props = fileData.createProperties();
            File f = new File(new File(path), META_DATA_FILE_NAME);
            OutputStream out = new FileOutputStream( f );
            props.store(out, "Document meta data");       
    }
    
    private List<String> getUuidList() {
        File file = new File(DIRECTORY);
        String[] directories = file.list(new FilenameFilter() {
          @Override
          public boolean accept(File current, String name) {
            return new File(current, name).isDirectory();
          }
        });
        return Arrays.asList(directories);
    }
    
    private Properties readProperties(String uuid) throws IOException {
        Properties prop = new Properties();
        InputStream input = null;     
        try {
            input = new FileInputStream(new File(getDirectoryPath(uuid),META_DATA_FILE_NAME));
            prop.load(input);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
    
    /**
     * loads the document from the data store with the given UUID.
     */
    @Override
    public FileData load(String uuid) {
        try {
            return loadFromFileSystem(uuid);
        } catch (IOException e) {
            String message = "Error while loading document with id: " + uuid;
            LOG.error(message, e);
            throw new RuntimeException(message, e);
        }
        
    }
    
    private String createDirectory(FileData fileData) {
        String path = getDirectoryPath(fileData);
        createDirectory(path);
        return path;
    }

    private String getDirectoryPath(FileData fileData) {
       return getDirectoryPath(fileData.getUuid());
    }
    
    private String getDirectoryPath(String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append(DIRECTORY).append(File.separator).append(uuid);
        String path = sb.toString();
        return path;
    }

    private void createDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
    }
	

}
