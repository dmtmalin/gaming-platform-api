package ru.iteco.core.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iteco.core.game.GameScreen;
import ru.iteco.core.game.GameScreenRepository;
import ru.iteco.core.game.GameScreenService;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.utility.StorageUtility;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/game/screen")
public class ApiGameScreenController {

    private final GameScreenService gameScreenService;
    private final StorageUtility storageUtility;
    private final GameScreenRepository gameScreenRepository;

    @Autowired
    public ApiGameScreenController(GameScreenService gameScreenService,
                                   StorageUtility storageUtility,
                                   GameScreenRepository gameScreenRepository) {
        this.gameScreenService = gameScreenService;
        this.storageUtility = storageUtility;
        this.gameScreenRepository = gameScreenRepository;
    }

    @PostMapping(value = "/create")
    public GameScreen createGameVersion(@Valid @RequestBody GameScreenCreateForm screenCreateForm) {
        String screenUrl = screenCreateForm.getScreenUrl();
        storageUtility.findFileOrThrow(screenUrl);
        return gameScreenService.create(screenCreateForm);
    }

    @PostMapping(value = "/change")
    public GameScreen change(@Valid @RequestBody GameScreenChangeForm gameScreenChangeForm) {
        Integer id = gameScreenChangeForm.getId();
        GameScreen gameScreen = gameScreenRepository.findOne(id);
        if (gameScreen == null)
            throw new EntityNotFoundException(String.format("Game screen with id %s not found", id));
        return gameScreenService.update(gameScreen, gameScreenChangeForm);
    }
}
