package ru.iteco.core.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "game")
@NamedEntityGraph(name = "Game.withCurrentGameVersion",
        attributeNodes = @NamedAttributeNode("currentGameVersion"))
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "profile_id", nullable = false)
    private Integer profileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_category_id", insertable = false, updatable = false)
    private GameCategory gameCategory;

    @JsonIgnore
    @Column(name = "game_category_id")
    private Integer gameCategoryFk;

    @Column(name = "icon", nullable = false)
    private String icon;

    @Column(name = "icon_small", nullable = false)
    private String iconSmall;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GameVersion> gameVersions;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_game_version_id", insertable = false, updatable = false)
    private GameVersion currentGameVersion;

    @JsonIgnore
    @Column(name = "current_game_version_id")
    private Integer currentGameVersionFk;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Date();
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public GameCategory getGameCategory() {
        return gameCategory;
    }

    public void setGameCategory(GameCategory gameCategory) {
        this.gameCategory = gameCategory;
    }

    public Integer getGameCategoryFk() {
        return gameCategoryFk;
    }

    public void setGameCategoryFk(Integer gameCategoryFk) {
        this.gameCategoryFk = gameCategoryFk;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconSmall() {
        return iconSmall;
    }

    public void setIconSmall(String iconSmall) {
        this.iconSmall = iconSmall;
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

    public Set<GameVersion> getGameVersions() {
        return gameVersions;
    }

    public void setGameVersions(Set<GameVersion> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public GameVersion getCurrentGameVersion() {
        return currentGameVersion;
    }

    public void setCurrentGameVersion(GameVersion currentGameVersion) {
        this.currentGameVersion = currentGameVersion;
    }

    public Integer getCurrentGameVersionFk() {
        return currentGameVersionFk;
    }

    public void setCurrentGameVersionFk(Integer currentGameVersionFk) {
        this.currentGameVersionFk = currentGameVersionFk;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
