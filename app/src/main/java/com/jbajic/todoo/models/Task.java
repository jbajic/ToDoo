package com.jbajic.todoo.models;

/**
 * Created by jure on 15.05.17..
 */

public class Task {

    private Long id;
    private Long serverId;
    private String name;
    private String body;
    private Boolean completed;
    private Integer estimatedTime;
    private Long categoryId;
    private Long projectId;

    public Task(Long id, Long serverId, String name, String body, Boolean completed, Integer estimatedTime, Long categoryId, Long projectId) {
        this.id = id;
        this.serverId = serverId;
        this.name = name;
        this.body = body;
        this.completed = completed;
        this.estimatedTime = estimatedTime;
        this.categoryId = categoryId;
        this.projectId = projectId;
    }

    public Task(Long serverId, String name, String body, Boolean completed, Integer estimatedTime, Long categoryId, Long projectId) {
        this.serverId = serverId;
        this.name = name;
        this.body = body;
        this.completed = completed;
        this.estimatedTime = estimatedTime;
        this.categoryId = categoryId;
        this.projectId = projectId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
