package ru.iteco.core.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.iteco.core.account.User;
import ru.iteco.core.account.UserService;
import ru.iteco.core.game.web.GameChangeForm;
import ru.iteco.core.game.web.GameCreateForm;
import ru.iteco.property.ImageThumbnailProperties;
import ru.iteco.service.StorageService;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private final ImageThumbnailProperties thumbnail;
    private final StorageService storageService;
    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public GameServiceImpl(ImageThumbnailProperties imageThumbnailProperties,
                           StorageService storageService,
                           GameRepository gameRepository,
                           UserService userService) {
        this.thumbnail = imageThumbnailProperties;
        this.storageService = storageService;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @Override
    public Game create(GameCreateForm form) {
        logger.info("Create game " + form.getName());
        Game game = new Game();
        User user = userService.currentUser();
        game.setName(form.getName());
        game.setDescription(form.getDescription());
        game.setProfileId(user.getProfileId());
        game.setUserId(user.getUserId());
        game.setGameCategoryFk(form.getCategoryId());
        game.setPublished(true);
        game.setPriority(0);
        String attachIconUrl = form.getIconUrl();
        Resource attachIcon = storageService.load(attachIconUrl);
        String iconUrl = storageService.store(attachIcon, GameCreateForm.UPLOAD_PREFIX);
        game.setIcon(iconUrl);
        String iconSmallUrl = storageService.thumbnail(
                attachIcon,
                thumbnail.getIconSmallWidth(),
                thumbnail.getIconSmallHeight(),
                GameCreateForm.UPLOAD_PREFIX);
        game.setIconSmall(iconSmallUrl);
        storageService.delete(attachIconUrl);
        return gameRepository.save(game);
    }

    @Override
    public Game update(Game game, GameChangeForm form) {
        logger.info("Update game " + form.getName());
        game.setName(form.getName());
        game.setDescription(form.getDescription());
        game.setGameCategoryFk(form.getCategoryId());
        game.setCurrentGameVersionFk(form.getCurrentGameVersionId());
        game.setPublished(form.getPublished());
        game.setPriority(form.getPriority());
        String attachIconUrl = form.getIconUrl();
        boolean iconIsChange = !game.getIcon().equals(attachIconUrl);
        if (iconIsChange) {
            Resource attachIcon = storageService.load(attachIconUrl);
            String iconUrl = storageService.store(attachIcon, GameCreateForm.UPLOAD_PREFIX);
            game.setIcon(iconUrl);
            String iconSmallUrl = storageService.thumbnail(
                    attachIcon,
                    thumbnail.getIconSmallWidth(),
                    thumbnail.getIconSmallHeight(),
                    GameCreateForm.UPLOAD_PREFIX);
            game.setIconSmall(iconSmallUrl);
            storageService.delete(attachIconUrl);
        }
        return gameRepository.save(game);
    }
}
