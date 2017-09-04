package ru.iteco.core.account;


public class User {
    private Integer userId;

    private Integer profileId;

    private User(Integer userId, Integer profileId) {
        this.userId = userId;
        this.profileId = profileId;
    }

    public static User valueOf(Integer userId, Integer profileId) {
        return new User(userId, profileId);
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
}
