package com.a1dnan.filesys.controller;


import com.a1dnan.filesys.entities.FileData;
import com.a1dnan.filesys.repositories.FileDataRepository;
import com.a1dnan.filesys.services.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final StorageService service;
    private final FileDataRepository repository;

    @PostMapping
    public ResponseEntity<FileData> uploadImageToFIleSystem(@RequestParam("file") MultipartFile file) throws IOException {

        FileData uploadFile = service.upload(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadFile);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> download(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        byte[] fileData=service.download(fileName);
        Optional<FileData> file = repository.findByName(fileName);
        String mimeType = request.getServletContext().getMimeType(file.get().getName());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + file.get().getName())
                .body(fileData);

    }

    @GetMapping
    public ResponseEntity<List<FileData>> getAllFiles(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllFiles());
    }



}
