package ru.iteco.core.game;

import ru.iteco.core.game.web.GameVersionChangeForm;
import ru.iteco.core.game.web.GameVersionCreateForm;

import java.io.IOException;

public interface GameVersionService {
    GameVersion create(GameVersionCreateForm form) throws IOException;

    GameVersion update(GameVersion gameVersion, GameVersionChangeForm form);
}
