package ru.iteco.constraint;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileIsZipValidator implements ConstraintValidator<FileIsZip, MultipartFile> {

    private List<byte[]> signatures;

    @Override
    public void initialize(FileIsZip fileIsArchive) {
        // Signatures of zip archive
        signatures = new ArrayList<byte[]>() {{
            add(new byte[] {80, 75, 3, 4});
            add(new byte[] {80, 75, 5, 6});
        }};
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFile == null || multipartFile.isEmpty())
            return true;
        int len = 4;
        byte[] firstBytes = new byte[len];
        try {
            InputStream stream = multipartFile.getInputStream();
            int readBytes = stream.read(firstBytes, 0, len);
            if (readBytes == len) {
                for(byte[] signature : signatures) {
                    boolean isEquals = Arrays.equals(firstBytes, signature);
                    if(isEquals)
                        return true;
                }
            }
            stream.close();
        }
        catch (IOException e) {
            return true;
        }
        return false;
    }
}
