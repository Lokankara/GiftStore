package com.store.gift.controller;

import com.store.gift.handler.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@CrossOrigin(origins = {
        "http://192.168.31.177:4200",
        "http://localhost:5500",
        "http://localhost:4200",
        "http://127.0.0.1:5500",
        "http://127.0.0.1:8080",
        "http://127.0.0.1:4200",
        "https://gift-store-angular.netlify.app",
        "https://gift-store-certificate.netlify.app",
        "https://gift-store.onrender.com"})
public class FileUploadController {

    @Value("${upload-dir}")
    private String dir;

    @GetMapping(value = "upload/{filename:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String filename) {
        try {
            Resource resource = new UrlResource(Paths.get(dir).resolve(filename).toUri());
            if (resource.exists() && resource.isReadable()) {
                MediaType mediaType = determineMediaType(filename);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(mediaType);

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType determineMediaType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    @PostMapping("/upload")
    public ResponseMessage uploadFile(
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseMessage.builder()
                    .errorMessage("Please select a file")
                    .statusCode(BAD_REQUEST)
                    .build();
        }
        String name = file.getOriginalFilename();
        if (name == null) {
            return ResponseMessage.builder()
                    .errorMessage("File name is null")
                    .statusCode(BAD_REQUEST)
                    .build();
        }
        Path uploadPath = Paths.get(dir, name);

        if (Files.exists(uploadPath.resolve(name))) {
            return ResponseMessage.builder()
                    .errorMessage("File already exists")
                    .statusCode(CONFLICT)
                    .build();
        }
        try {
            Files.write(uploadPath, file.getBytes());
        } catch (IOException e) {
            return ResponseMessage.builder()
                    .errorMessage("error write File")
                    .statusCode(BAD_REQUEST)
                    .build();
        }
        return ResponseMessage
                .builder()
                .errorMessage("File uploaded successfully")
                .statusCode(OK)
                .build();
    }
}
