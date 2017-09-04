package ru.iteco.core.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.iteco.service.StorageService;
import ru.iteco.utility.ZipUtility;

import java.io.IOException;
import java.io.InputStream;


@Component
public class GameVersionCreateFormValidator implements Validator {

    private final StorageService storageService;
    private final ZipUtility zipUtility;
    private final MessageSource messageSource;

    @Autowired
    public GameVersionCreateFormValidator(StorageService storageService, ZipUtility zipUtility, MessageSource messageSource) {
        this.storageService = storageService;
        this.zipUtility = zipUtility;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(GameVersionCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors())
            return;
        GameVersionCreateForm form = (GameVersionCreateForm) target;
        String archiveUrl = form.getArchiveUrl();
        boolean contentIsEmpty = StringUtils.isEmpty(archiveUrl) && StringUtils.isEmpty(form.getRemoteUrl());
        if(contentIsEmpty) {
            String message = messageSource.getMessage(
                    "ru.iteco.core.game.web.ContentIsEmpty", null, LocaleContextHolder.getLocale());
            errors.reject("ContentIsEmpty", message);
        }
        if(!validateEntryHtmlFile(archiveUrl)) {
            String message = messageSource.getMessage(
                    "ru.iteco.core.game.web.GameEntryFileNotFound", null, LocaleContextHolder.getLocale());
            errors.reject("GameEntryFileNotFound", message);
        }
    }

    private boolean validateEntryHtmlFile(String archiveUrl) {
        if (StringUtils.isEmpty(archiveUrl) || !storageService.exists(archiveUrl)) {
            return true;
        }
        else {
            try {
                Resource archive = storageService.load(archiveUrl);
                InputStream stream = archive.getInputStream();
                boolean isContain = zipUtility.containsFile(stream, "index.html");
                stream.close();
                return isContain;
            } catch (IOException e) {
                return true;
            }
        }
    }
}
