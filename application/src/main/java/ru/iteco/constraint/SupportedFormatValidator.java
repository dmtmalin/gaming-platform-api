package ru.iteco.constraint;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class SupportedFormatValidator implements ConstraintValidator<SupportedFormat, MultipartFile> {

    private String[] supportedFormat;

    @Override
    public void initialize(SupportedFormat image) {
        this.supportedFormat = image.value().split("\\s+");
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty())
            return true;
        String filename = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(filename);
        return Arrays.asList(supportedFormat).contains(extension);
    }
}
