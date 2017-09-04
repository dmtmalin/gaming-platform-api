package ru.iteco.core.game.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GameCreateForm {

    public static final String UPLOAD_PREFIX = "icon/";

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    @Size(min = 20, max = 512)
    private String description;

    @NotNull
    private String iconUrl;

    private Integer categoryId;

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
