package com.kazan.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "USER_GROUP_ROLE")
public class UserGroupRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6698307116255363423L;

	@Id
	@Column(name="user_id")
	private Integer userId;	
	
	@Id
	@Column(name="group_id")
	private Integer groupId;	
	
	@Id
	@Column(name="role_id")
	private Integer roleId;	
	
	@Column(name="group_alias")
	private String groupAlias;
	
	@Column(name="symbol_master")
	private String symbolMaster;
	
	@Column(name = "expiry_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;
	
	@Column(name="active")
	private Integer active;

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getGroupAlias() {
		return groupAlias;
	}

	public void setGroupAlias(String groupAlias) {
		this.groupAlias = groupAlias;
	}

	public String getSymbolMaster() {
		return symbolMaster;
	}

	public void setSymbolMaster(String symbolMaster) {
		this.symbolMaster = symbolMaster;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((groupAlias == null) ? 0 : groupAlias.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((symbolMaster == null) ? 0 : symbolMaster.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserGroupRole other = (UserGroupRole) obj;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (groupAlias == null) {
			if (other.groupAlias != null)
				return false;
		} else if (!groupAlias.equals(other.groupAlias))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (symbolMaster == null) {
			if (other.symbolMaster != null)
				return false;
		} else if (!symbolMaster.equals(other.symbolMaster))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
