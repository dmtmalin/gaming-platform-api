package ru.iteco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.iteco.service.StorageService;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfiguration {

    private final StorageFactory storageFactory;

    @Autowired
    public StorageConfiguration(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    @Bean
    StorageService storageService() {
        return storageFactory.build();
    }
}
