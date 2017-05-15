package com.jbajic.todoo.models;

/**
 * Created by jure on 15.05.17..
 */

public class User {

    private Long id;
    private Long serverId;
    private String email;
    private String imagePath;
    private String username;
    private Boolean isMe;

    public User(Long id, Long serverId, String email, String imagePath, String username, Boolean isMe) {
        this.id = id;
        this.serverId = serverId;
        this.email = email;
        this.imagePath = imagePath;
        this.username = username;
        this.isMe = isMe;
    }

    public User(Long serverId, String email, String imagePath, String username, Boolean isMe) {
        this.serverId = serverId;
        this.email = email;
        this.imagePath = imagePath;
        this.username = username;
        this.isMe = isMe;
    }

    public Long getId() {
        return id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getMe() {
        return isMe;
    }

    public void setMe(Boolean me) {
        isMe = me;
    }
}
