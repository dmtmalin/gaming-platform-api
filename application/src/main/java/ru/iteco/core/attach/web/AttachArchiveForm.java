package ru.iteco.core.attach.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;
import ru.iteco.constraint.FileIsZip;
import ru.iteco.constraint.FileNotEmpty;

import javax.validation.constraints.NotNull;

public class AttachArchiveForm implements AttachObjectForm {

    @NotNull
    @FileNotEmpty
    @FileIsZip
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
