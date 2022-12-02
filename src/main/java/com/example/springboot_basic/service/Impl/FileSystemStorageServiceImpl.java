package com.example.springboot_basic.service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.example.springboot_basic.config.StorageProperties;
import com.example.springboot_basic.exception.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springboot_basic.service.StorageService;

import javax.annotation.Resource;

@Service
public class FileSystemStorageServiceImpl implements StorageService{
    private final Path rootLocation;

    @Override
    public String getStorageFileName(MultipartFile file, String id){
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return "p" + id + "." + ext;
    }

    public FileSystemStorageServiceImpl(StorageProperties properties){   //StorageProperties là file setting ở folder: config/StorageProperties.java
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, String storedFilename) {   //lưu file với tên của file được define ở storedFilename.. file: là file image lấy từ VIEW.  **storedFilename: lưu image vào entity.getImage
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFilename))   //resolve: dùng để path dựa trên rootLocation. exg: root: c/nmcong.. -> resolve: c/nmcong/fileName
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())){
                throw new StorageException("Cannot store file outside current directory");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch(Exception e){
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {   //for display images in addOrEdit.html
        try {
            Path file = load(filename);
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());   //URI syntax: scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
            if (resource.exists() || resource.isReadable()){
                return (Resource) resource;
            }
            throw new StorageException("Could not read file: " + filename);
        }catch (Exception e){
            throw new StorageException("Could not read file: " + filename);
        }
    }

    @Override
    public Path load(String filename){
        return rootLocation.resolve(filename);   //resolve: dùng để path dựa trên rootLocation. exg: root: c/nmcong.. -> resolve: c/nmcong/fileName
    }

    @Override
    public void delete(String storedFilename) throws IOException{
        Path destinationFile = rootLocation.resolve(Paths.get(storedFilename))
                .normalize().toAbsolutePath();
        Files.delete(destinationFile);
    }

    @Override
    public void init() throws Exception {
        try{
            Files.createDirectories(rootLocation);
            System.out.println(rootLocation.toString());
        }catch (Exception e){
            throw new Exception("Could not initialize storage", e);
        }
    }

}
