package ru.iteco.core.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.iteco.core.game.web.GameScreenChangeForm;
import ru.iteco.core.game.web.GameScreenCreateForm;
import ru.iteco.property.ImageThumbnailProperties;
import ru.iteco.service.StorageService;

@Service
public class GameScreenServiceImpl implements GameScreenService {

    private final GameScreenRepository gameScreenRepository;
    private final StorageService storageService;
    private final ImageThumbnailProperties thumbnail;

    @Autowired
    public GameScreenServiceImpl(GameScreenRepository gameScreenRepository,
                                 StorageService storageService,
                                 ImageThumbnailProperties thumbnail) {
        this.gameScreenRepository = gameScreenRepository;
        this.storageService = storageService;
        this.thumbnail = thumbnail;
    }

    @Override
    public GameScreen create(GameScreenCreateForm form) {
        GameScreen gameScreen = new GameScreen();
        gameScreen.setGameFk(form.getGameId());
        gameScreen.setPublished(true);
        gameScreen.setPriority(0);
        String attachScreenUrl = form.getScreenUrl();
        Resource attachScreen = storageService.load(attachScreenUrl);
        String screenUrl = storageService.store(attachScreen, GameScreenCreateForm.UPLOAD_PREFIX);
        gameScreen.setScreen(screenUrl);
        String screenSmallUrl = storageService.thumbnail(
                attachScreen,
                thumbnail.getScreenSmallWidth(),
                thumbnail.getScreenSmallHeight(),
                GameScreenCreateForm.UPLOAD_PREFIX);
        gameScreen.setScreenSmall(screenSmallUrl);
        storageService.delete(attachScreenUrl);
        return gameScreenRepository.save(gameScreen);
    }

    @Override
    public GameScreen update(GameScreen gameScreen, GameScreenChangeForm form) {
        gameScreen.setPriority(form.getPriority());
        gameScreen.setPublished(form.getPublished());
        return gameScreenRepository.save(gameScreen);
    }
}
