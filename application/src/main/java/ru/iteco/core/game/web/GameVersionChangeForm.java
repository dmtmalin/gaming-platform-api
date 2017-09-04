package ru.iteco.core.game.web;

import ru.iteco.core.game.GameVersionStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameVersionChangeForm {
    @NotNull
    private Integer id;

    @Size(max = 512)
    private String whatNew;

    @NotNull
    private GameVersionStatus status;

    @Size(max = 255)
    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWhatNew() {
        return whatNew;
    }

    public void setWhatNew(String whatNew) {
        this.whatNew = whatNew;
    }

    public GameVersionStatus getStatus() {
        return status;
    }

    public void setStatus(GameVersionStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
