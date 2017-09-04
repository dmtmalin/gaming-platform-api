package ru.iteco.core.attach.web;

import org.springframework.web.multipart.MultipartFile;

public interface AttachObjectForm {
    void setUri(String uri);

    String getUri();

    MultipartFile getFile();

    void setFile(MultipartFile file);
}
