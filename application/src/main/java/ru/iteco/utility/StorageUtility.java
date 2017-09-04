package ru.iteco.utility;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.iteco.error.FileNotFoundException;
import ru.iteco.service.StorageService;

import java.io.File;

@Component
public class StorageUtility {

    private StorageService storageService;

    public StorageUtility(StorageService storageService) {
        this.storageService = storageService;
    }

    public void findFileOrThrow(String uri) {
        if(!storageService.exists(uri) || StringUtils.isEmpty(uri)) {
            throw new FileNotFoundException(String.format("File %s not found", new File(uri).getName()));
        }
    }
}
