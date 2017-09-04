package ru.iteco.core.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "game_version")
public class GameVersion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "build", nullable = false)
    private Integer build;

    @Column(name = "uri", nullable = false, length = 2000)
    private String uri;

    @Column(name = "what_new", length = 512)
    private String whatNew;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    private Game game;

    @Column(name = "game_id", nullable = false)
    private Integer gameFk;

    @Column(name = "status", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private GameVersionStatus status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_remote", nullable = false)
    private Boolean isRemote;

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

    public Integer getBuild() {
        return build;
    }

    public void setBuild(Integer build) {
        this.build = build;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getWhatNew() {
        return whatNew;
    }

    public void setWhatNew(String whatNew) {
        this.whatNew = whatNew;
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

    public Boolean getRemote() {
        return isRemote;
    }

    public void setRemote(Boolean remote) {
        isRemote = remote;
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
