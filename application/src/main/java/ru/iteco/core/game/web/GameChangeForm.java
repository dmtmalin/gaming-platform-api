package ru.iteco.core.game.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameChangeForm {
    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 20, max = 512)
    private String description;

    @NotNull
    private String iconUrl;

    @NotNull
    private Boolean isPublished;

    @NotNull
    private Integer priority;

    private Integer categoryId;

    private Integer currentGameVersionId;

    public GameChangeForm() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCurrentGameVersionId() {
        return currentGameVersionId;
    }

    public void setCurrentGameVersionId(Integer currentGameVersionId) {
        this.currentGameVersionId = currentGameVersionId;
    }
}
