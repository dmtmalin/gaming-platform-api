package ru.iteco.core.game.web;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameVersionCreateForm {

    public static final String UPLOAD_PREFIX = "game/";

    @NotNull
    private Integer gameId;

    @Size(max = 512)
    private String whatNew;

    private String archiveUrl;

    @URL
    private String remoteUrl;

    public GameVersionCreateForm() {
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getWhatNew() {
        return whatNew;
    }

    public void setWhatNew(String whatNew) {
        this.whatNew = whatNew;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }
}
