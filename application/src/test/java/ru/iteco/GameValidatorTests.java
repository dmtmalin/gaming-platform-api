package ru.iteco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import ru.iteco.config.MessageSourceConfiguration;
import ru.iteco.core.game.Game;
import ru.iteco.core.game.GameRepository;
import ru.iteco.core.game.web.GameCreateForm;
import ru.iteco.core.game.web.GameCreateFormValidator;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
@ContextConfiguration(classes = MessageSourceConfiguration.class)
public class GameValidatorTests {
    @MockBean
    private GameRepository gameRepository;
    @Autowired
    private MessageSource messageSource;

    @Test
    public void gameAlreadyExistsTest() throws Exception {
        String name = "testName";
        Game game = new Game();
        game.setName(name);
        GameCreateForm form = new GameCreateForm();
        form.setName(name);
        when(gameRepository.findOneByName(Matchers.isA(String.class))).thenReturn(game);
        Errors errors = new BeanPropertyBindingResult(form, "gameCreateForm");
        GameCreateFormValidator validator = new GameCreateFormValidator(gameRepository, messageSource);
        validator.validate(form, errors);
        assertTrue(errors.hasErrors());
        assertEquals(errors.getErrorCount(), 1);
        assertNotNull(errors.getGlobalError());
        assertEquals(errors.getGlobalError().getObjectName(), "gameCreateForm");
        assertEquals(errors.getGlobalError().getCode(), "AlreadyExists");
        assertNotNull(errors.getGlobalError().getDefaultMessage());
    }
}
