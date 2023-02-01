package com.accenture.ra.rql.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.ra.rql.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUser(List<SearchCriteria> params) {
    	log.info("searchUser - START");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        UserSearchQueryCriteriaConsumer<User> searchConsumer = 
          new UserSearchQueryCriteriaConsumer<>(criteriaBuilder, root);
        
        params
        	.stream()
        	.forEach(searchConsumer);
        
        Predicate predicate = searchConsumer
        	.getPredicate();
        
        query
        	.where(predicate);

        List<User> result = entityManager
        	.createQuery(query)
        	.getResultList();
        
        return result;
    }

    @Override
    @Transactional
    public void save(User entity) {
    	log.info("saveUser - START");
        entityManager.persist(entity);
    }

	@Override
	@Transactional
	public void saveAll(User... users) {
		Stream
			.of(users)
			.forEach(this::save);
	}

	@Override
	public List<User> findAll() {
    	log.info("findAll - START");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        
        Root<User> root = query
        	.from(User.class);	
       
        query
        	.select(root);
        
        return entityManager
        		.createQuery(query)
        		.getResultList();
        
       }
}