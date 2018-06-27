package com.kazan.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kazan.model.KazanGroup;

@Repository
public class GroupRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public int getCreatorByGroupId(int groupId) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from KazanGroup where group_id = :groupIdToSelect");
			query.setParameter("groupIdToSelect", groupId);
			KazanGroup result = (KazanGroup) query.uniqueResult();
			return result.getCreator();
		} catch (Exception e) {
			System.out.println("GroupRepository.getCreatorByUserGroup:" + e);
			return -1;
		}
	}
	
	@Transactional
	public KazanGroup getGroupById(int groupId) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from KazanGroup where group_id = :groupIdToSelect");
			query.setParameter("groupIdToSelect", groupId);
			query.setMaxResults(1);
			KazanGroup result = (KazanGroup) query.uniqueResult();
			if (null == result)
				return new KazanGroup();
			else
				return result;
		} catch (Exception e) {
			System.out.println("GroupRepository.getGroupById:" + e);
			return new KazanGroup();
		}
	}
	
	@Transactional
	public List<KazanGroup> getGroupByIdList(List<Integer> groupIds) {
		if (null == groupIds || 0 == groupIds.size())
			return new ArrayList<KazanGroup>();
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("from KazanGroup where group_id in (:groupIdListToSelect)");
			query.setParameterList("groupIdListToSelect", groupIds);
			List<KazanGroup> result = query.list();
			if (null == result)
				return new ArrayList<KazanGroup>();
			else
				return result;
		} catch (Exception e) {		
			System.out.println("GroupRepository.getGroupByIdList:" + e);
			return new ArrayList<KazanGroup>();
		}
	}
	
	@Transactional
	public List<KazanGroup> getAll() {
		try {
			return sessionFactory.getCurrentSession().createQuery("from KazanGroup").list();
		} catch (Exception e) {		
			System.out.println("GroupRepository.getAll:" + e);
			return new ArrayList<KazanGroup>();
		}
	}
	
	@Transactional
	public KazanGroup add(KazanGroup kazanGroup) {
		sessionFactory.getCurrentSession().persist(kazanGroup);
		sessionFactory.getCurrentSession().flush();
		return kazanGroup;
	}

	@Transactional
	public KazanGroup update(KazanGroup kazanGroup) {
		sessionFactory.getCurrentSession().merge(kazanGroup);
		return kazanGroup;
	}
}
