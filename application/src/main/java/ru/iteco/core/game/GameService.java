package ru.iteco.core.game;

import ru.iteco.core.game.web.GameChangeForm;
import ru.iteco.core.game.web.GameCreateForm;

public interface GameService {

    Game create(GameCreateForm form);

    Game update(Game game, GameChangeForm form);
}
