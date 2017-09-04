package ru.iteco.service;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.iteco.StorageException;
import ru.iteco.StorageProperties;

import java.io.IOException;
import java.io.InputStream;

@Service
public class WebDavStorageService extends StorageServiceImpl {

    private final String rootUri;
    private final Sardine sardine;

    @Autowired
    public WebDavStorageService(StorageProperties properties) {
        this.rootUri = properties.getRootUri();
        this.sardine = SardineFactory.begin();
    }

    @Override
    public String store(String filename, byte[] bytes, String prefix) throws StorageException {
        String url = rootUri + prefix + filename;
        try {
            int copy = 0;
            while (sardine.exists(url)) {
                copy += 1;
                String copyFilename = getCopiesFilename(filename, copy);
                url = rootUri + prefix + copyFilename;
            }
            sardine.put(url, bytes);
        } catch (IOException e) {
            throw new StorageException("Cannot store file: " + url, e);
        }
        return prefix + filename;
    }

    @Override
    public Resource load(String uri) throws StorageException {
        uri = rootUri + uri;
        try {
            InputStream stream = sardine.get(uri);
            return new InputStreamResource(stream);
        } catch (IOException e) {
            throw new StorageException("Cannot get file: " + uri, e);
        }
    }

    @Override
    public void delete(String uri) throws StorageException {
        uri = rootUri + uri;
        try {
            sardine.delete(uri);
        } catch (IOException e) {
            throw new StorageException("Cannot delete file: " + uri, e);
        }
    }

    @Override
    public boolean exists(String uri) {
        uri = rootUri + uri;
        try {
            return sardine.exists(uri);
        } catch (IOException e) {
            return false;
        }
    }
}
