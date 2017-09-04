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
public class GameChangeFormValidator implements Validator {

    private final GameRepository gameRepository;
    private final MessageSource messageSource;

    @Autowired
    public GameChangeFormValidator(GameRepository gameRepository, MessageSource messageSource) {
        this.gameRepository = gameRepository;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(GameChangeForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors())
            return;
        GameChangeForm form = (GameChangeForm) target;
        Game game = gameRepository.findOneByName(form.getName());
        if (game != null) {
            boolean thisGame = game.getId().equals(form.getId());
            if (!thisGame) {
                String message = messageSource.getMessage(
                        "ru.iteco.core.game.web.AlreadyExists", new Object[] { form.getName() }, LocaleContextHolder.getLocale());
                errors.reject("AlreadyExists", message);
            }
        }
    }
}
