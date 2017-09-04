package ru.iteco.constraint;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

public class ImageResolutionValidator implements ConstraintValidator<ImageResolution, MultipartFile> {

    private int width;
    private int height;

    @Override
    public void initialize(ImageResolution image) {
        this.width = image.width();
        this.height = image.height();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty())
            return true;
        try {
            InputStream stream = multipartFile.getInputStream();
            Object obj = ImageIO.createImageInputStream(stream);
            ImageReader reader = ImageIO.getImageReaders(obj).next();
            reader.setInput(obj);
            int imageWidth = reader.getWidth(0);
            int imageHeight = reader.getHeight(0);
            return imageWidth == width && imageHeight == height;
        }
        catch (NoSuchElementException e) {
            // it's not image
            return false;
        }
        catch (IOException e) {
            return true;
        }
    }
}
