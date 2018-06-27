package com.kazan.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GROUPS")
public class KazanGroup {
	@Id
	@GeneratedValue
	@Column(name = "group_id")
	private Integer groupId;

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "group_notify_bot")
	private String groupNotifyBot;

	@Column(name = "group_alert_bot")
	private String groupAlertBot;

	@Column(name = "group_image")
	private String groupImage;

	@Column(name = "mt4_account")
	private String mt4Account;

	@Column(name = "mt4_server")
	private String mt4Server;

	@Column(name = "mt4_password")
	private String mt4Password;

	@Column(name = "notify_value")
	private Double notifyValue;

	@Column(name = "notify_object_type")
	private String notifyObjectType;

	@Column(name = "notify_re_time")
	private String notifyReTime;

	@Column(name = "creater")
	private Integer creator;

	@Column(name = "group_private")
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

	public String getTokenBot(int botType) {
		if (null == groupNotifyBot && null == groupAlertBot)
			return "";
		if (botType == 1) {
			if (null != groupNotifyBot) {
				return groupNotifyBot;
			} else {
				return groupAlertBot;
			}
		} else if (botType == 2) {
			if (null != groupAlertBot) {
				return groupAlertBot;
			} else {
				return groupNotifyBot;
			}
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((groupAlertBot == null) ? 0 : groupAlertBot.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupImage == null) ? 0 : groupImage.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((groupNotifyBot == null) ? 0 : groupNotifyBot.hashCode());
		result = prime * result + ((groupPrivate == null) ? 0 : groupPrivate.hashCode());
		result = prime * result + ((mt4Account == null) ? 0 : mt4Account.hashCode());
		result = prime * result + ((mt4Password == null) ? 0 : mt4Password.hashCode());
		result = prime * result + ((mt4Server == null) ? 0 : mt4Server.hashCode());
		result = prime * result + ((notifyObjectType == null) ? 0 : notifyObjectType.hashCode());
		result = prime * result + ((notifyReTime == null) ? 0 : notifyReTime.hashCode());
		result = prime * result + ((notifyValue == null) ? 0 : notifyValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KazanGroup other = (KazanGroup) obj;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (groupAlertBot == null) {
			if (other.groupAlertBot != null)
				return false;
		} else if (!groupAlertBot.equals(other.groupAlertBot))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (groupImage == null) {
			if (other.groupImage != null)
				return false;
		} else if (!groupImage.equals(other.groupImage))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (groupNotifyBot == null) {
			if (other.groupNotifyBot != null)
				return false;
		} else if (!groupNotifyBot.equals(other.groupNotifyBot))
			return false;
		if (groupPrivate == null) {
			if (other.groupPrivate != null)
				return false;
		} else if (!groupPrivate.equals(other.groupPrivate))
			return false;
		if (mt4Account == null) {
			if (other.mt4Account != null)
				return false;
		} else if (!mt4Account.equals(other.mt4Account))
			return false;
		if (mt4Password == null) {
			if (other.mt4Password != null)
				return false;
		} else if (!mt4Password.equals(other.mt4Password))
			return false;
		if (mt4Server == null) {
			if (other.mt4Server != null)
				return false;
		} else if (!mt4Server.equals(other.mt4Server))
			return false;
		if (notifyObjectType == null) {
			if (other.notifyObjectType != null)
				return false;
		} else if (!notifyObjectType.equals(other.notifyObjectType))
			return false;
		if (notifyReTime == null) {
			if (other.notifyReTime != null)
				return false;
		} else if (!notifyReTime.equals(other.notifyReTime))
			return false;
		if (notifyValue == null) {
			if (other.notifyValue != null)
				return false;
		} else if (!notifyValue.equals(other.notifyValue))
			return false;
		return true;
	}

}
