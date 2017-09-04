package ru.iteco.core.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game_screen")
public class GameScreen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private Game game;

    @Column(name = "game_id", nullable = false)
    private Integer gameFk;

    @Column(name = "screen", nullable = false)
    private String screen;

    @Column(name = "screen_small", nullable = false)
    private String screenSmall;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getGameFk() {
        return gameFk;
    }

    public void setGameFk(Integer gameFk) {
        this.gameFk = gameFk;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getScreenSmall() {
        return screenSmall;
    }

    public void setScreenSmall(String screenSmall) {
        this.screenSmall = screenSmall;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
