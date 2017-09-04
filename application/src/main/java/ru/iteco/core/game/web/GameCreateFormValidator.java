package ru.iteco.core.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.iteco.core.game.Game;
import ru.iteco.core.game.GameRepository;

@Component
public class GameCreateFormValidator implements Validator {

    private final GameRepository gameRepository;
    private final MessageSource messageSource;

    @Autowired
    public GameCreateFormValidator(GameRepository gameRepository, MessageSource messageSource) {
        this.gameRepository = gameRepository;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(GameCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors())
            return;
        GameCreateForm form = (GameCreateForm) target;
        Game game = gameRepository.findOneByName(form.getName());
        if (game != null) {
            String message = messageSource.getMessage(
                    "ru.iteco.core.game.web.AlreadyExists", new Object[] { form.getName() }, LocaleContextHolder.getLocale());
            errors.reject("AlreadyExists", message);
        }
    }
}
