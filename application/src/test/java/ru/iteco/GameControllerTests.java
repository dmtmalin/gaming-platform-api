package ru.iteco;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.context.WebApplicationContext;
import ru.iteco.config.WebSecurityConfiguration;
import ru.iteco.core.game.Game;
import ru.iteco.core.game.GameRepository;
import ru.iteco.core.game.GameService;
import ru.iteco.core.game.web.ApiGameController;
import ru.iteco.core.game.web.GameChangeFormValidator;
import ru.iteco.core.game.web.GameCreateForm;
import ru.iteco.core.game.web.GameCreateFormValidator;
import ru.iteco.utility.StorageUtility;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ApiGameController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class),
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ru.iteco.core.account.security.*")})
@AutoConfigureMockMvc(secure = false)
public class GameControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private GameService gameService;
    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private GameCreateFormValidator gameCreateFormValidator;
    @MockBean
    private GameChangeFormValidator gameChangeFormValidator;
    @MockBean
    private StorageUtility storageUtility;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // supports any classes for validator
        when(gameCreateFormValidator.supports(any())).thenReturn(true);
    }

    @Test
    public void gameCreateEmptyNameTest() throws Exception {
        GameCreateForm form = new GameCreateForm();
        form.setName("");
        form.setDescription("Minimum 20 symbols for this field");
        form.setIconUrl("attach/icon.mock");
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("Size")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("name")))
                .andExpect(jsonPath("errors[0].value", is("")));
    }

    @Test
    public void gameCreateOverSizeNameTest() throws Exception {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        GameCreateForm form = new GameCreateForm();
        String name = generator.generate(256);
        form.setName(name);
        form.setDescription("Minimum 20 symbols for this field");
        form.setIconUrl("attach/icon.mock");
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("Size")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("name")))
                .andExpect(jsonPath("errors[0].value", is(name)));
    }

    @Test
    public void gameCreateNullNameTest() throws Exception {
        GameCreateForm form = new GameCreateForm();
        form.setName(null);
        form.setDescription("Minimum 20 symbols for this field");
        form.setIconUrl("attach/icon.mock");
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("NotNull")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("name")))
                .andExpect(jsonPath("errors[0].value", is(nullValue())));
    }

    @Test
    public void gameCreateEmptyDescriptionTest() throws Exception {
        GameCreateForm form = new GameCreateForm();
        form.setName("testName");
        form.setDescription("");
        form.setIconUrl("attach/icon.mock");
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("Size")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("description")))
                .andExpect(jsonPath("errors[0].value", is("")));
    }

    @Test
    public void gameCreateNullDescriptionTest() throws Exception {
        GameCreateForm form = new GameCreateForm();
        form.setName("testName");
        form.setDescription(null);
        form.setIconUrl("attach/icon.mock");
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("NotNull")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("description")))
                .andExpect(jsonPath("errors[0].value", is(nullValue())));
    }

    @Test
    public void gameCreateOverSizeDescriptionTest() throws Exception {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        GameCreateForm form = new GameCreateForm();
        String description = generator.generate(513);
        form.setName("testName");
        form.setDescription(description);
        form.setIconUrl("attach/icon.mock");
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("Size")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("description")))
                .andExpect(jsonPath("errors[0].value", is(description)));
    }

    @Test
    public void gameCreateNullIconTest() throws Exception {
        GameCreateForm form = new GameCreateForm();
        form.setName("testName");
        form.setDescription("Minimum 20 symbols for this field");
        form.setIconUrl(null);
        mockMvc.perform(
                post("/game/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(form)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("path", is("/game/create")))
                .andExpect(jsonPath("exception", containsString("MethodArgumentNotValidException")))
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0].code", is("NotNull")))
                .andExpect(jsonPath("errors[0].objectName", is("gameCreateForm")))
                .andExpect(jsonPath("errors[0].type", is("FIELD_ERROR")))
                .andExpect(jsonPath("errors[0].field", is("iconUrl")))
                .andExpect(jsonPath("errors[0].value", is(nullValue())));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
