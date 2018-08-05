package com.kazan.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kazan.model.KazanGroup;
import com.kazan.model.KazanUser;

@Repository
public class UserRepository {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public KazanUser add(KazanUser kazanUser) {
		sessionFactory.getCurrentSession().persist(kazanUser);
		sessionFactory.getCurrentSession().flush();
		return kazanUser;		
	}

	@Transactional
	public KazanUser update(KazanUser kazanUser) {
		sessionFactory.getCurrentSession().merge(kazanUser);
		return kazanUser;
	}
	
	@Transactional
	public KazanUser getById(Integer id) {
		return (KazanUser) sessionFactory.getCurrentSession().get(KazanUser.class, id);
	}
	
	@Transactional
	public int getIdByUsername(String username) {
		if (null == username)
			return -1;
		KazanUser result = (KazanUser) geByUsername(username);
		if (null == result)
			return -1;
		else
			return result.getUserId();
	}	
	
	@Transactional
	public KazanUser geByUsername(String username) {
		Query query = sessionFactory.getCurrentSession().createQuery("from KazanUser where username = :usernameToSelect ");
		query.setParameter("usernameToSelect", username);
		return (KazanUser) query.uniqueResult();
	}
	
	@Transactional
	public List<KazanUser> getUserByIdList(List<Integer> userIds) {
		if (null == userIds || 0 == userIds.size())
			return new ArrayList<KazanUser>();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from KazanUser where user_id in (:userIdListToSelect)");
			query.setParameterList("userIdListToSelect", userIds);
			List<KazanUser> result = query.list();
			if (null == result)
				return new ArrayList<KazanUser>();
			else
				return result;
		} catch (Exception e) {		
			System.out.println("UserRepository.getUserByIdList:" + e);
			return new ArrayList<KazanUser>();
		}
	}
	
	@Transactional
	public int geTelegramId(int userId) {
		Query query = sessionFactory.getCurrentSession().createQuery("from KazanUser where user_Id = :userIdToSelect ");
		query.setParameter("userIdToSelect", userId);
		KazanUser result = (KazanUser) query.uniqueResult();
		if(null != result) {
			return result.getTelegramId();
		} else {
			return 0;
		}
		
	}

}
