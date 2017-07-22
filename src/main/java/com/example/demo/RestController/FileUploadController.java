/**
 * 
 */
package com.example.demo.RestController;



import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.domain.FileData;
import com.example.demo.domain.FileMetaData;
import com.example.demo.service.IFileAccessService;

/**
 * @author Sushma
 *
 */
@RestController
@RequestMapping(value = "/demo")
public class FileUploadController {
	
private static final Logger LOG = Logger.getLogger(FileUploadController.class);
    
    @Autowired
    IFileAccessService fileAccessService;

    /**
     * Adds a file to the archive.
     * 
     * Url: /demo/upload?file={file}&person={person}&date={date} [POST]
     * 
     * @param file A file posted in a multipart request
     * @param username The name of the uploading person
     * @param date The date file was uploaded
     * @return The meta data of the added file
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody FileMetaData handleFileUpload(
            @RequestParam(value="file", required=true) MultipartFile file ,
            @RequestParam(value="person", required=true) String person,
            @RequestParam(value="date", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        
        try {
        	FileData fileData = new FileData(file.getBytes(), file.getOriginalFilename(), date, person );
        	getFileAccessService().save(fileData);
            return fileData.getMetadata();
        } catch (RuntimeException e) {
            LOG.error("Error while uploading.", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error while uploading.", e);
            throw new RuntimeException(e);
        }      
    }
    
    /**
     * Finds file in the archive. Returns a list of files 
     * 
     * Url: /demo/files?person={person}&date={date} [GET]
     * 
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public HttpEntity<List<FileMetaData>> findDocument(
            @RequestParam(value="person", required=false) String person,
            @RequestParam(value="date", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<FileMetaData>>(getFileAccessService().findFiles(person,date), httpHeaders,HttpStatus.OK);
    }
    
    /**
     * Returns the  file from the archive with the given UUID.
     * 
     * Url: /demo/file/{id} [GET]
     * 
     */
    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getDocument(@PathVariable String id) {         
        // send it back to the client
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(getFileAccessService().getFileId(id), httpHeaders, HttpStatus.OK);
    }

    public IFileAccessService getFileAccessService() {
        return fileAccessService;
    }

    public void setArchiveService(IFileAccessService fileAccessService) {
        this.fileAccessService = fileAccessService;
    }

	
}
