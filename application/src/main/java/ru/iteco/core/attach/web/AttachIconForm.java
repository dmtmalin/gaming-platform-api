package ru.iteco.core.attach.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.FileNotEmpty;
import ru.iteco.constraint.ImageResolution;
import ru.iteco.constraint.SupportedFormat;

import javax.validation.constraints.NotNull;

public class AttachIconForm implements AttachObjectForm {
    @NotNull
    @FileNotEmpty
    @SupportedFormat(value = "jpg jpeg png")
    @ImageResolution(width = 150, height = 200)
    @JsonIgnore
    private MultipartFile file;

    private String uri;

    @Override
    public MultipartFile getFile() {
        return file;
    }

    @Override
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getUri() {
        return uri;
    }
}
