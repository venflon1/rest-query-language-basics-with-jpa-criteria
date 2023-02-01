package com.accenture.ra.rql.dao;

public record SearchUserFilter(
  	 String id,
	 String firstname,
	 String lastname,
	 String username,
   	 String email,
   	 String age,
   	 String rangeMinAge,
   	 String rangeMaxAge
   ) { }