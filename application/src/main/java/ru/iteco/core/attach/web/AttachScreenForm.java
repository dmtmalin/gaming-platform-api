package ru.iteco.core.attach.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.FileNotEmpty;
import ru.iteco.constraint.ImageMinResolution;
import ru.iteco.constraint.SupportedFormat;

import javax.validation.constraints.NotNull;


public class AttachScreenForm implements AttachObjectForm {
    @NotNull
    @FileNotEmpty
    @SupportedFormat(value = "jpg jpeg png")
    @ImageMinResolution(width = 640, height = 480)
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
