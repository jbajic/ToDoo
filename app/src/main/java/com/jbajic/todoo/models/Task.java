package com.jbajic.todoo.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jure on 15.05.17..
 */

public class Task implements Serializable {

    public enum TaskType {
        task,
        category
    }

    private Long id;
    private Long serverId;
    private String name;
    private String body;
    private Boolean completed;
    private Integer estimatedTime;
    private Long categoryId;
    private Long projectId;
    private Long userId;
    private List<Task> taskList;

    public Task(Long id, Long serverId, String name, String body, Boolean completed, Integer estimatedTime, Long categoryId, Long projectId, Long userId) {
        this.id = id;
        this.serverId = serverId;
        this.name = name;
        this.body = body;
        this.completed = completed;
        this.estimatedTime = estimatedTime;
        this.categoryId = categoryId;
        this.projectId = projectId;
        this.userId = userId;
    }

    public Task(Long serverId, String name, String body, Boolean completed, Integer estimatedTime, Long categoryId, Long projectId, Long userId) {
        this.serverId = serverId;
        this.name = name;
        this.body = body;
        this.completed = completed;
        this.estimatedTime = estimatedTime;
        this.categoryId = categoryId;
        this.projectId = projectId;
        this.userId = userId;
    }

    public Task(String name, String body, Boolean completed, Integer estimatedTime, Long categoryId, Long projectId, Long userId) {
        this.name = name;
        this.body = body;
        this.completed = completed;
        this.estimatedTime = estimatedTime;
        this.categoryId = categoryId;
        this.projectId = projectId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return this.getName();
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
