package ru.iteco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.iteco.StorageException;
import ru.iteco.StorageFileNotFoundException;
import ru.iteco.StorageProperties;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService extends StorageServiceImpl {
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        String rootUri = properties.getRootUri();
        this.rootLocation = Paths.get(rootUri);
    }

    @Override
    public String store(String filename, byte[] bytes, String prefix) throws StorageException {
        String cleanPath = StringUtils.cleanPath(prefix + filename);
        Path cleanFilename = this.rootLocation.resolve(cleanPath);
        if (cleanPath.contains("..")) {
            // This is a security check
            throw new StorageException(
                    "Cannot store file with relative path outside current directory " + filename);
        }
        int copy = 0;
        while (Files.exists(cleanFilename)) {
            copy += 1;
            String copyFilename = getCopiesFilename(filename, copy);
            cleanPath = StringUtils.cleanPath(prefix + copyFilename);
            cleanFilename = this.rootLocation.resolve(cleanPath);
        }
        try {
            Path parent = cleanFilename.getParent();
            if(!Files.exists(parent))
                Files.createDirectories(parent);
            InputStream stream = new ByteArrayInputStream(bytes);
            Files.copy(stream, cleanFilename, StandardCopyOption.REPLACE_EXISTING);
            stream.close();
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return cleanPath;
    }

    @Override
    public Resource load(String uri) throws StorageException {
        try {
            Path filename = rootLocation.resolve(uri);
            Resource resource = new UrlResource(filename.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + uri, e);
        }
    }

    @Override
    public void delete(String uri) throws StorageException {
        File file = this.rootLocation.resolve(uri).toFile();
        if(!file.delete()) {
            throw new StorageException("Cannot delete file: " + uri);
        }
    }

    @Override
    public boolean exists(String uri) {
        Path file = this.rootLocation.resolve(uri);
        return Files.exists(file);
    }
}
