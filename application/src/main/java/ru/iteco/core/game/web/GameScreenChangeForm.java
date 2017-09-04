package ru.iteco.core.game.web;

import javax.validation.constraints.NotNull;

public class GameScreenChangeForm {
    @NotNull
    private Integer id;

    @NotNull
    private Integer priority;

    @NotNull
    private Boolean isPublished;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }
}
