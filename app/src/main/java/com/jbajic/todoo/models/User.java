package com.jbajic.todoo.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jure on 15.05.17..
 */

public class User implements Serializable {

    private Long id;
    private Long serverId;
    private String email;
    private String username;
    private String fName;
    private String lName;
    private String address;
    private String city;
    private Boolean isMe;
    private ArrayList<Project> projects;

    public User(Long id, Long serverId, String email, String username, String fName, String lName, String address, String city, Boolean isMe) {
        this.id = id;
        this.serverId = serverId;
        this.email = email;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.city = city;
        this.isMe = isMe;
    }

    public User(Long serverId, String email, String username, String fName, String lName, String address, String city, Boolean isMe) {
        this.serverId = serverId;
        this.email = email;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.city = city;
        this.isMe = isMe;
    }

    public User(Long id, Long serverId, String email, String username, String fName, String lName, String address, String city, Boolean isMe, ArrayList<Project> projects) {
        this.id = id;
        this.serverId = serverId;
        this.email = email;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.address = address;
        this.city = city;
        this.isMe = isMe;
        this.projects = projects;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getMe() {
        return isMe;
    }

    public void setMe(Boolean me) {
        isMe = me;
    }

    public ArrayList<Project> getProjects() {
        if(projects != null) {
            return projects;
        } else {
            return new ArrayList<Project>();
        }

    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        if(this.getMe()) {
            return "Me";
        }
        return this.getfName() + " " + this.getlName();
    }

}
