package ru.iteco.core.game.web;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.iteco.core.game.Game;
import ru.iteco.core.game.GameRepository;
import ru.iteco.core.game.GameService;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.utility.StorageUtility;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/game")
public class ApiGameController {
    private final GameService gameService;
    private final GameRepository gameRepository;
    private final GameCreateFormValidator gameCreateFormValidator;
    private final GameChangeFormValidator gameChangeFormValidator;
    private final StorageUtility storageUtility;

    public ApiGameController(GameService gameService,
                             GameRepository gameRepository,
                             GameCreateFormValidator gameCreateFormValidator,
                             GameChangeFormValidator gameChangeFormValidator,
                             StorageUtility storageUtility) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.gameCreateFormValidator = gameCreateFormValidator;
        this.gameChangeFormValidator = gameChangeFormValidator;
        this.storageUtility = storageUtility;
    }

    @InitBinder("gameCreateForm")
    public void bindGameCreateForm(WebDataBinder binder) {
        binder.addValidators(gameCreateFormValidator);
    }

    @InitBinder("gameChangeForm")
    public void bindGameChangeForm(WebDataBinder binder) {
        binder.addValidators(gameChangeFormValidator);
    }

    @PostMapping(value = "/create")
    public Game create(@Valid @RequestBody GameCreateForm gameCreateForm) {
        String attachIconUrl = gameCreateForm.getIconUrl();
        storageUtility.findFileOrThrow(attachIconUrl);
        return gameService.create(gameCreateForm);
    }

    @PostMapping(value = "/change")
    public Game change(@Valid @RequestBody GameChangeForm gameChangeForm) {
        String iconUrl = gameChangeForm.getIconUrl();
        storageUtility.findFileOrThrow(iconUrl);
        Integer id = gameChangeForm.getId();
        Game game = gameRepository.findOne(id);
        if (game == null)
            throw new EntityNotFoundException(String.format("Game with id %s not found", id));
        return gameService.update(game, gameChangeForm);
    }
}
