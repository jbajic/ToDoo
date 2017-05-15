package com.jbajic.todoo.models;

/**
 * Created by jure on 15.05.17..
 */

public class Project {

    private Long id;
    private Long serverId;
    private String name;
    private String body;
    private String client;
    private String deadline;
    private Boolean completed;
    private Long managerId;

    public Project(Long id, Long serverId, String name, String body, String client, String deadline, Boolean completed, Long managerId) {
        this.id = id;
        this.serverId = serverId;
        this.name = name;
        this.body = body;
        this.client = client;
        this.deadline = deadline;
        this.completed = completed;
        this.managerId = managerId;
    }

    public Project(Long serverId, String name, String body, String client, String deadline, Boolean completed, Long managerId) {
        this.serverId = serverId;
        this.name = name;
        this.body = body;
        this.client = client;
        this.deadline = deadline;
        this.completed = completed;
        this.managerId = managerId;
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

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
