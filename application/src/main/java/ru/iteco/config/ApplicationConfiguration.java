package ru.iteco.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.iteco.property.ImageThumbnailProperties;
import ru.iteco.property.MalwareScanProperties;

@Configuration
@EnableConfigurationProperties({
        ImageThumbnailProperties.class,
        MalwareScanProperties.class
})
@EnableAsync
public class ApplicationConfiguration {
}
