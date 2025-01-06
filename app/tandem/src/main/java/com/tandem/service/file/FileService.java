package com.tandem.service.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    @Autowired
    private ResourceLoader resourceLoader;

    public File getPngFileAsFile() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:userlogo.png");
        return resource.getFile();
    }

    public InputStream getPngFileAsInputStream() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:userlogo.png");
        return resource.getInputStream();
    }
}