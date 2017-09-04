package ru.iteco.core.game.web;

import javax.validation.constraints.NotNull;

public class GameScreenCreateForm {

    public static String UPLOAD_PREFIX = "screen/";

    @NotNull
    private Integer gameId;

    @NotNull
    private String screenUrl;

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getScreenUrl() {
        return screenUrl;
    }

    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
    }
}
