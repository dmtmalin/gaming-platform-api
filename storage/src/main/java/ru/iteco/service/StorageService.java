package ru.iteco.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.StorageException;

public interface StorageService {

    String store(MultipartFile file, String prefix) throws StorageException;

    String store(MultipartFile file) throws StorageException;

    String store(String filename, byte[] bytes, String prefix) throws StorageException;

    String store(String filename, byte[] bytes) throws StorageException;

    String store(Resource resource, String prefix) throws StorageException;

    String store(Resource resource) throws StorageException;

    Resource load(String uri) throws StorageException;

    String thumbnail(MultipartFile file, int width, int height, String prefix);

    String thumbnail(MultipartFile file, int width, int height);

    String thumbnail(Resource resource, int width, int height, String prefix);

    String thumbnail(Resource resource, int width, int height);

    void delete(String uri) throws StorageException;

    boolean exists(String uri);
}
