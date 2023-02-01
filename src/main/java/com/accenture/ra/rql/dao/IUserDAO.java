package com.accenture.ra.rql.dao;

import java.util.List;

import com.accenture.ra.rql.entity.User;

public interface IUserDAO {
	 List<User> findAll();
	 List<User> searchUser(List<SearchCriteria> searchParams);
	 void save(User entity);
	 void saveAll(User...users);
}