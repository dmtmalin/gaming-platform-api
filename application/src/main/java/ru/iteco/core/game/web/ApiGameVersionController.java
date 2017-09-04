package ru.iteco.core.game.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.iteco.core.game.GameVersion;
import ru.iteco.core.game.GameVersionRepository;
import ru.iteco.core.game.GameVersionService;
import ru.iteco.error.ApplicationException;
import ru.iteco.error.EntityNotFoundException;
import ru.iteco.utility.StorageUtility;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "/game/version")
public class ApiGameVersionController {

    private final GameVersionService gameVersionService;
    private final GameVersionRepository gameVersionRepository;
    private final StorageUtility storageUtility;
    private final GameVersionCreateFormValidator gameVersionCreateFormValidator;
    private final GameVersionChangeFormValidator gameVersionChangeFormValidator;

    @Autowired
    public ApiGameVersionController(GameVersionService gameVersionService,
                                    GameVersionRepository gameVersionRepository,
                                    StorageUtility storageUtility,
                                    GameVersionCreateFormValidator gameVersionCreateFormValidator,
                                    GameVersionChangeFormValidator gameVersionChangeFormValidator) {
        this.gameVersionService = gameVersionService;
        this.gameVersionRepository = gameVersionRepository;
        this.storageUtility = storageUtility;
        this.gameVersionCreateFormValidator = gameVersionCreateFormValidator;
        this.gameVersionChangeFormValidator = gameVersionChangeFormValidator;
    }

    @InitBinder("gameVersionCreateForm")
    public void bindGameCreateForm(WebDataBinder binder) {
        binder.addValidators(gameVersionCreateFormValidator);
    }

    @InitBinder("gameVersionChangeForm")
    public void bindGameChangeForm(WebDataBinder binder) {
        binder.addValidators(gameVersionChangeFormValidator);
    }

    @PostMapping(value = "/create")
    public GameVersion createGameVersion(@Valid @RequestBody GameVersionCreateForm gameVersionCreateForm) {
        String archiveUrl = gameVersionCreateForm.getArchiveUrl();
        storageUtility.findFileOrThrow(archiveUrl);
        try {
            return gameVersionService.create(gameVersionCreateForm);
        } catch (IOException e) {
            throw new ApplicationException("Can't create version", e);
        }
    }

    @PostMapping(value = "/change")
    public GameVersion changeGameVersion(@Valid @RequestBody GameVersionChangeForm gameVersionChangeForm) {
        Integer id = gameVersionChangeForm.getId();
        GameVersion gameVersion = gameVersionRepository.findOne(id);
        if (gameVersion == null) {
            throw new EntityNotFoundException(String.format("Game version with id %s not found", id));
        }
        return gameVersionService.update(gameVersion, gameVersionChangeForm);
    }
}
