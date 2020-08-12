package com.example.FileOperations.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.FileOperations.exception.MyFileNotFoundException;
import com.example.FileOperations.service.FileStorageService;

@RestController
@RequestMapping("/app")
public class FileController
{
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/copyFile")
    public ResponseEntity<String> copyFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path)
    {
        String fileName = fileStorageService.storeFile(file, path);
        return ResponseEntity.ok().body(fileName + " copied to path : " + path + "/temp");
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadFile(@RequestParam("name") String fileName, @RequestParam("path") String path,
        HttpServletRequest request)
    {

        Resource resource = fileStorageService.loadFileAsResource(fileName, path);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }

    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(@RequestParam("name") String fileName, @RequestParam("path") String path,
        HttpServletRequest request) throws IOException
    {
        Resource resource = fileStorageService.loadFileAsResource(fileName, path);
        if (!resource.getFile().delete()) {
            new MyFileNotFoundException("File not found");
        }
        return ResponseEntity.ok().body("File deleted");
    }

}
