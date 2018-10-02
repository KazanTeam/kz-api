package com.kazan.dto;

public class KazanGroupDto {

    private Integer groupId;

    private String groupName;

    private String groupNotifyBot;

    private String groupAlertBot;

    private String groupImage;

    private String mt4Account;

    private String mt4Server;

    private String mt4Password;

    private Double notifyValue;

    private String notifyObjectType;

    private String notifyReTime;

    private Integer creator;

    private Integer groupPrivate;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNotifyBot() {
        return groupNotifyBot;
    }

    public void setGroupNotifyBot(String groupNotifyBot) {
        this.groupNotifyBot = groupNotifyBot;
    }

    public String getGroupAlertBot() {
        return groupAlertBot;
    }

    public void setGroupAlertBot(String groupAlertBot) {
        this.groupAlertBot = groupAlertBot;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getMt4Account() {
        return mt4Account;
    }

    public void setMt4Account(String mt4Account) {
        this.mt4Account = mt4Account;
    }

    public String getMt4Server() {
        return mt4Server;
    }

    public void setMt4Server(String mt4Server) {
        this.mt4Server = mt4Server;
    }

    public String getMt4Password() {
        return mt4Password;
    }

    public void setMt4Password(String mt4Password) {
        this.mt4Password = mt4Password;
    }

    public Double getNotifyValue() {
        return notifyValue;
    }

    public void setNotifyValue(Double notifyValue) {
        this.notifyValue = notifyValue;
    }

    public String getNotifyObjectType() {
        return notifyObjectType;
    }

    public void setNotifyObjectType(String notifyObjectType) {
        this.notifyObjectType = notifyObjectType;
    }

    public String getNotifyReTime() {
        return notifyReTime;
    }

    public void setNotifyReTime(String notifyReTime) {
        this.notifyReTime = notifyReTime;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getGroupPrivate() {
        return groupPrivate;
    }

    public void setGroupPrivate(Integer groupPrivate) {
        this.groupPrivate = groupPrivate;
    }
}
