package ru.iteco.service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.StorageException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.nio.file.Files;


public abstract class StorageServiceImpl implements StorageService {

    @Override
    public String thumbnail(MultipartFile file, int width, int height, String prefix) {
        byte[] thumbnailBytes = resize(file, width, height);
        String originalFilename = file.getOriginalFilename();
        String filename = getThumbnailFilename(originalFilename, width, height);
        return store(filename, thumbnailBytes, prefix);
    }

    @Override
    public String thumbnail(MultipartFile file, int width, int height) {
        return thumbnail(file, width, height, "");
    }

    @Override
    public String thumbnail(Resource resource, int width, int height, String prefix) {
        byte[] thumbnailBytes = resize(resource, width, height);
        String originalFilename = resource.getFilename();
        String filename = getThumbnailFilename(originalFilename, width, height);
        return store(filename, thumbnailBytes, prefix);
    }

    @Override
    public String thumbnail(Resource resource, int width, int height) {
        return thumbnail(resource, width, height, "");
    }

    @Override
    public String store(MultipartFile file, String prefix) throws StorageException {
        byte[] bytes;
        try {
            bytes = file.getBytes();
        }
        catch (IOException e) {
            throw new StorageException("Cannot read for store file: " + file.getName(), e);
        }
        String filename = file.getOriginalFilename();
        return store(filename, bytes, prefix);
    }

    @Override
    public String store(MultipartFile file) throws StorageException {
        return store(file, "");
    }

    @Override
    public String store(String filename, byte[] bytes) throws StorageException {
        return store(filename, bytes, "");
    }

    @Override
    public String store(Resource resource, String prefix) throws StorageException {
        byte[] bytes;
        try {
            File file = resource.getFile();
            bytes = Files.readAllBytes(file.toPath());
        }
        catch (IOException e) {
            throw new StorageException("Cannot read for store file: " + resource.getFilename(), e);
        }
        return store(resource.getFilename(), bytes, prefix);
    }

    @Override
    public String store(Resource resource) throws StorageException {
        return store(resource, "");
    }

    @Override
    public String store(String filename, byte[] bytes, String prefix) throws StorageException {
        throw new NotImplementedException();
    }

    @Override
    public void delete(String uri) throws StorageException {
        throw new NotImplementedException();
    }

    @Override
    public boolean exists(String uri) {
        throw new NotImplementedException();
    }

    @Override
    public Resource load(String uri) throws StorageException {
        throw new NotImplementedException();
    }

    private byte[] resize(MultipartFile file, int width, int height)
            throws StorageException {
        byte[] thumbnailBytes;
        try {
            InputStream stream = file.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(stream)
                    .size(width, height)
                    .toOutputStream(outputStream);
            thumbnailBytes = outputStream.toByteArray();
            outputStream.close();
        }
        catch (IOException e) {
            throw new StorageException("Cannot thumbnail file: " + file.getName());
        }
        return thumbnailBytes;
    }

    private byte[] resize(Resource resource, int width, int height)
            throws StorageException {
        byte[] thumbnailBytes;
        try {
            File file = resource.getFile();
            InputStream stream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(stream)
                    .size(width, height)
                    .toOutputStream(outputStream);
            thumbnailBytes = outputStream.toByteArray();
            stream.close();
            outputStream.close();
        }
        catch (IOException e) {
            throw new StorageException("Cannot thumbnail file: " + resource.getFilename());
        }
        return thumbnailBytes;
    }

    protected String getThumbnailFilename(String filename, int width, int height) {
        String extension = FilenameUtils.getExtension(filename);
        String baseName = FilenameUtils.getBaseName(filename);
        return String.format("%s_%s_%s.%s", baseName, width, height, extension);
    }

    protected String getCopiesFilename(String filename, int copy) {
        String extension = FilenameUtils.getExtension(filename);
        String baseName = FilenameUtils.getBaseName(filename);
        return String.format("%s%s.%s", baseName, copy, extension);
    }
}
