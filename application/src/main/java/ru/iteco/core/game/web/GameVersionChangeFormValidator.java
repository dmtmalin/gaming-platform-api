package ru.iteco.core.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.iteco.core.game.GameVersionStatus;

@Component
public class GameVersionChangeFormValidator implements Validator {

    private final MessageSource messageSource;

    @Autowired
    public GameVersionChangeFormValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(GameVersionChangeForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GameVersionChangeForm form = (GameVersionChangeForm) target;
        GameVersionStatus status = form.getStatus();
        if (status == GameVersionStatus.REJECT && StringUtils.isEmpty(form.getReason())) {
            String message = messageSource.getMessage(
                    "ru.iteco.core.game.web.ReasonIsEmpty", null, LocaleContextHolder.getLocale());
            errors.reject("ReasonIsEmpty", message);
        }
    }
}
