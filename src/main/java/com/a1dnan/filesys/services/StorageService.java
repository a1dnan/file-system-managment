package com.a1dnan.filesys.services;

import com.a1dnan.filesys.entities.FileData;
import com.a1dnan.filesys.repositories.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private FileDataRepository fileDataRepository;
    private Path fileStoragePath;
    private String fileStorageLocation;

    public StorageService(@Value("${file.storage.location:STORAGE_FOLDER}") String fileStorageLocation) {

        this.fileStorageLocation = fileStorageLocation;
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public FileData upload(MultipartFile file) throws IOException {
        String filePath=fileStoragePath+"\\"+file.getOriginalFilename();

        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(file.getOriginalFilename())
                .toUriString();

        FileData fileData=fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .url(url)
                .build()
        );

        file.transferTo(new File(filePath));
        return fileData;
    }

    public byte[] download(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath=fileData.get().getFilePath();
        byte[] file = Files.readAllBytes(new File(filePath).toPath());
        return file;
    }

    public List<FileData> getAllFiles(){
        return fileDataRepository.findAll();
    }


}
