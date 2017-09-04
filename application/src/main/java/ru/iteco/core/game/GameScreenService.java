package ru.iteco.core.game;

import ru.iteco.core.game.web.GameScreenChangeForm;
import ru.iteco.core.game.web.GameScreenCreateForm;

public interface GameScreenService {
    GameScreen create(GameScreenCreateForm form);

    GameScreen update(GameScreen gameScreen, GameScreenChangeForm form);
}
