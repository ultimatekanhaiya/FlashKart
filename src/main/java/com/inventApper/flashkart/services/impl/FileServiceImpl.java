package com.inventApper.flashkart.services.impl;

import com.inventApper.flashkart.exceptions.BadApiRequestException;
import com.inventApper.flashkart.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFileName = file.getOriginalFilename();
        logger.info("Uploading fileName : {}", originalFileName);
        String fileName = UUID.randomUUID().toString();
        String extension = Objects.requireNonNull(originalFileName).substring(originalFileName.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + fileNameWithExtension;

        logger.info("full image path : {}", fullPathWithFileName);

        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            // file save
            File folder = new File(path);

            if (!folder.exists()) {
                // create the folder
                folder.mkdirs();
            }

            // upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequestException("File with extension " + extension + " not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
