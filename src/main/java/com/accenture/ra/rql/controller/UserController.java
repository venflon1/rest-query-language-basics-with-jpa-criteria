package com.accenture.ra.rql.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.ra.rql.dao.IUserDAO;
import com.accenture.ra.rql.dao.SearchCriteria;
import com.accenture.ra.rql.entity.User;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

	@Autowired
	private IUserDAO userDao;
	
	@GetMapping
	public ResponseEntity<List<User>> getAll(){
		return ResponseEntity.ok(
			this.userDao.findAll()
		);
	}
	
	@GetMapping(path = "/search")
	public ResponseEntity<List<User>> seachUserBy(
			@RequestParam(value = "q", required = false) String search ) {
		log.info("{}", search);
		
		List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if ( !Objects.isNull(search) ) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(
                	new SearchCriteria(
                		// key
            			matcher.group(1), 
            			// operation
            			matcher.group(2), 
            			// value
            			matcher.group(3)
                	)
               );
            }
        }
		
		return ResponseEntity.ok(
			this.userDao.searchUser(params)
		);
	}
}