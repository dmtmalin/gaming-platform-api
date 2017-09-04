package ru.iteco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.iteco.service.FileSystemStorageService;
import ru.iteco.service.StorageService;
import ru.iteco.service.WebDavStorageService;

@Component
public class StorageFactory {

    private final FileSystemStorageService fileSystemStorageService;
    private final WebDavStorageService webDavStorageService;
    private final StorageProperties properties;

    @Autowired
    public StorageFactory(FileSystemStorageService fileSystemStorageService,
                          WebDavStorageService webDavStorageService,
                          StorageProperties properties) {
        this.fileSystemStorageService = fileSystemStorageService;
        this.webDavStorageService = webDavStorageService;
        this.properties = properties;
    }

    public StorageService build() {
        StorageType storageType = properties.getStorageType();
        if (storageType == StorageType.FILESYSTEM) {
            return fileSystemStorageService;
        } else if (storageType == StorageType.WEBDAV) {
            return webDavStorageService;
        }
        return null;
    }
}
