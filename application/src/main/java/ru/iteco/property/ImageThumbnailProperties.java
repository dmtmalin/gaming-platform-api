package ru.iteco.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.image-thumbnail")
public class ImageThumbnailProperties {
    private Integer iconSmallWidth = 100;

    private Integer iconSmallHeight = 100;

    private Integer screenSmallWidth = 100;

    private Integer screenSmallHeight = 100;

    public Integer getIconSmallWidth() {
        return iconSmallWidth;
    }

    public void setIconSmallWidth(Integer iconSmallWidth) {
        this.iconSmallWidth = iconSmallWidth;
    }

    public Integer getIconSmallHeight() {
        return iconSmallHeight;
    }

    public void setIconSmallHeight(Integer iconSmallHeight) {
        this.iconSmallHeight = iconSmallHeight;
    }

    public Integer getScreenSmallWidth() {
        return screenSmallWidth;
    }

    public void setScreenSmallWidth(Integer screenSmallWidth) {
        this.screenSmallWidth = screenSmallWidth;
    }

    public Integer getScreenSmallHeight() {
        return screenSmallHeight;
    }

    public void setScreenSmallHeight(Integer screenSmallHeight) {
        this.screenSmallHeight = screenSmallHeight;
    }
}
