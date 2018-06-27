package com.kazan.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kazan.model.KazanGroup;
import com.kazan.model.UserGroupRole;

@Repository
public class UserGroupRoleRepository {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public int getGroupIdByGroupAlias(int userId, String groupAlias) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from UserGroupRole where userId = :userIdToSelect and groupAlias = :aliasToSelect ");
			query.setParameter("userIdToSelect", userId);
			query.setParameter("aliasToSelect", groupAlias);
			query.setMaxResults(1);
			System.out.println(userId + ":" + groupAlias);
			UserGroupRole result = (UserGroupRole) query.uniqueResult();
			if (null == result)
				return -1;
			else
				return result.getGroupId();
		} catch (Exception e) {		
			System.out.println("UserGroupRoleRepository.getGroupIdByGroupAlias:" + e);
			return -1;
		}
	}
	
	
	@Transactional
	public int getByGroupIdUserIdSymbol(int userId, int groupId, String symbol) {
		Query query = sessionFactory.getCurrentSession().createQuery("from UserGroupRole where userId = :userIdToSelect and groupId = :groupIdToSelect ");
		query.setParameter("userIdToSelect", userId);
		query.setParameter("groupIdToSelect", groupId);
		query.setMaxResults(1);
		UserGroupRole result = (UserGroupRole) query.uniqueResult();
		if (null == result)
			return -1;
		else {
			if(result.getRoleId()== 1 || result.getRoleId()== 2) return 2;
			if(result.getRoleId()== 3) {
				if (null != result.getSymbolMaster()) {
					String[] listSymbolMasters = result.getSymbolMaster().split(",");
					for(String symbolMaster: listSymbolMasters) {
						if(symbol.equalsIgnoreCase(symbolMaster)) return 2;
					}
				}
				return 3;
			}
			if(result.getRoleId()== 4) {
				if(result.getExpiryDate()!=null && result.getExpiryDate().before(new Date())) return 5;
				return 4;
			}
		}
		return -1;
	}
	
	@Transactional
	public int getByUserIdGroupId(int userId, int groupId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from UserGroupRole where userId = :userIdToSelect and groupId = :groupIdToSelect ");
		query.setParameter("userIdToSelect", userId);
		query.setParameter("groupIdToSelect", groupId);
		query.setMaxResults(1);
		UserGroupRole result = (UserGroupRole) query.uniqueResult();
		if (null == result)
			return -1;
		else {
			if(result.getRoleId()== 1 || result.getRoleId()== 2) return 2;
			if(result.getRoleId()== 3) return 3;
			if(result.getRoleId()== 4) {
				if(result.getExpiryDate()!=null && result.getExpiryDate().before(new Date())) return 5;
				return 4;
			}
		}
		return -1;
	}
	
	@Transactional
	public int getRoleIdByUserIdGroupId(int userId, int groupId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from UserGroupRole where userId = :userIdToSelect and groupId = :groupIdToSelect ");
		query.setParameter("userIdToSelect", userId);
		query.setParameter("groupIdToSelect", groupId);
		query.setMaxResults(1);
		UserGroupRole result = (UserGroupRole) query.uniqueResult();
		if (null == result)
			return -1;
		else {
			return result.getRoleId();
		}
	}
	
	@Transactional
	public List<Integer> getGroupIdListByUserId(int userId) {
		Query query = sessionFactory.getCurrentSession().createQuery("select distinct groupId from UserGroupRole where userId = :userIdToSelect ");
		query.setParameter("userIdToSelect", userId);
		List<Integer> result = query.list();
		if (null == result)
			return new ArrayList<Integer>();
		else {
			return result;
		}		
	}
	
	@Transactional
	public List<Integer> getUserIdByGroupIdAndMode(int groupId, int mode) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from UserGroupRole where groupId = :groupIdToSelect and roleId <= :modeToSelect ");
			query.setParameter("modeToSelect", mode);
			query.setParameter("groupIdToSelect", groupId);
//			query.setMaxResults(1);
			List<UserGroupRole >result = (List<UserGroupRole>) query.list();
			
//			List results =  query.list();
//			List<UserGroupRole> UserGroupRoles = new ArrayList<UserGroupRole>();
			
			List<Integer> listUserId= new ArrayList<Integer>();
			
			if (null != result) {
				int userId;
				for(UserGroupRole userGroupRole:result) {
					userId = userGroupRole.getUserId();
					if(0!=userId) listUserId.add(userId);
				}
			}
			return listUserId;
		} catch (Exception e) {		
			System.out.println("UserGroupRoleRepository.getUserIdByGroupIdAndMode:" + e);
			return new ArrayList<Integer>();
		}
	}
	
	@Transactional
	public UserGroupRole add(UserGroupRole userGroupRole) {
		sessionFactory.getCurrentSession().persist(userGroupRole);
		sessionFactory.getCurrentSession().flush();
		return userGroupRole;		
	}

	@Transactional
	public UserGroupRole update(UserGroupRole userGroupRole) {
		sessionFactory.getCurrentSession().merge(userGroupRole);
		return userGroupRole;
	}
	
	// add new KazanGroup and new UserGroupRole
	@Transactional
	public int add(KazanGroup kazanGroup, int userId, int roleId) {
		int groupId = (Integer) sessionFactory.getCurrentSession().save(kazanGroup);
		UserGroupRole userGroupRole = new UserGroupRole();
		userGroupRole.setGroupId(kazanGroup.getGroupId());
		userGroupRole.setUserId(userId);
		userGroupRole.setRoleId(roleId);
		sessionFactory.getCurrentSession().save(userGroupRole);
		sessionFactory.getCurrentSession().flush();
		return groupId;
	}
	
	// add new KazanGroup and new UserGroupRole
	@Transactional
	public int add(KazanGroup kazanGroup) {
		int groupId = (Integer) sessionFactory.getCurrentSession().save(kazanGroup);
		UserGroupRole userGroupRole = new UserGroupRole();
		userGroupRole.setGroupId(kazanGroup.getGroupId());
		userGroupRole.setUserId(kazanGroup.getCreator());
		userGroupRole.setRoleId(1);
		userGroupRole.setGroupAlias(kazanGroup.getGroupName());
		userGroupRole.setExpiryDate(new Date());
		userGroupRole.setActive(1);
		sessionFactory.getCurrentSession().save(userGroupRole);
		sessionFactory.getCurrentSession().flush();
		return groupId;
	}
	
	@Transactional
	public int removeUserFromGroup(int userId, int groupId) {
		String hql = "delete from KazanGroup where user_id=:userIdToDelete and group_id=:groupIdToDelete";
		return sessionFactory.getCurrentSession().createQuery(hql)
				.setInteger("userIdToDelete", userId).setInteger("groupIdToDelete", groupId)
				.executeUpdate();		
	}
}
