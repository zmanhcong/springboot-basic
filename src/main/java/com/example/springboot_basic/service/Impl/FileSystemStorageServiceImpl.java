package com.example.springboot_basic.service.Impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.example.springboot_basic.config.StorageProperties;
import com.example.springboot_basic.exception.StorageException;
import com.example.springboot_basic.exception.StorageFileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springboot_basic.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService{
    private final Path rootLocation;

    @Override
    public String getStorageFileName(MultipartFile file, String id){    //file: là tên file gốc, ví dụ:anh-dep.png, output của function này sẽ đổi tên ảnh, để không bị trùng lặp
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        return "p" + id + "." + ext;
    }

    public FileSystemStorageServiceImpl(StorageProperties properties){   //StorageProperties là file setting ở folder: config/StorageProperties.java
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, String storedFilename) {   //lưu file, với tham số đầu là file lấy từ view, tham số thứ 2 là tên file(tên file đã được đặt random)
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFilename))   //resolve: dùng để path dựa trên rootLocation. exg: root: c/nmcong.. -> resolve: c/nmcong/fileName
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())){   //So sánh path lấy ở trên và path lấy ở application.properties. Nếu file lưu ở ngoài stored_location(ví dụ: c:/nmcong/images) thì sẽ hiển thị message là không đc lưu ở ngoài folder
                throw new StorageException("Cannot store file outside current directory");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch(Exception e){
            throw new StorageException("Failed to store file", e);  //nếu mà không lưu được thì báo lỗi, có truyền thông tin của exception.
        }
    }

    @Override
    public Resource loadAsResource(String filename) {   //ta có tên file, mang tên file đó tìm trong folder rồi load for display images in addOrEdit.html
        try {
            Path file = load(filename);
            org.springframework.core.io.Resource resource = new UrlResource(file.toUri());   //URI chứa rất nhiêu thông tin, ví dụ: path: c:/nmcong/upload/image.png, file name, schema:file...vv..v URI syntax: scheme:[//[user:password@]host[:port]][/]path[?query][#fragment]
            if (resource.exists() || resource.isReadable()){
                return resource;
            }
            throw new StorageFileNotFoundException("Could not read file: " + filename);
        }catch (Exception e){
            throw new StorageFileNotFoundException("Could not read file: " + filename);
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

    //Hàm init này là tạo thư mục uploads/image. và  được init bằng @Bean ở function main().
    @Override
    public void init() throws Exception {
        try{
            Files.createDirectories(rootLocation);
            System.out.println(rootLocation.toString());
        }catch (Exception e){
            throw new StorageException("Could not initialize storage", e);
        }
    }

}
