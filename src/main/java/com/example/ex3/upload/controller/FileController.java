package com.example.ex3.upload.controller;

import com.example.ex3.upload.exception.UploadNotSupportedException;
import com.example.ex3.upload.util.UploadUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j2
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final UploadUtil uploadUtil;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("files") MultipartFile[] files) {
        log.info("Uploading files");

        if(files == null || files.length == 0) {
            throw new UploadNotSupportedException("No files to upload");
        }

        for (MultipartFile file : files) {
            log.info("--------------------");
            log.info("name: " + file.getOriginalFilename());
            checkFileType(file.getOriginalFilename());
        }
        List<String> result = uploadUtil.upload(files);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{fileName")
    public ResponseEntity<Void> deleteFile(@PathVariable(name = "fileName") String fileName) {
        log.info("delte file: " + fileName);
        uploadUtil.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    private void checkFileType(String filename) throws UploadNotSupportedException {
        //jpg, gif, png, bmp
        String suffix = filename.substring(filename.lastIndexOf(".")+1);
        String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bmp|BMP)";
        if(!suffix.matches(regExp)) {
            throw new UploadNotSupportedException("File type not supported: " + suffix);
        }
    }


}
