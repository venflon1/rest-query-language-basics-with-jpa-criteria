package com.accenture.ra.rql.dao;

import java.util.function.Consumer;

import com.accenture.ra.rql.entity.User;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
public class UserSearchQueryCriteriaConsumer<T> implements Consumer<SearchCriteria> {
	private Predicate predicate;
	private CriteriaBuilder criteriaBuilder;
	private Root<T> root;
	
	public UserSearchQueryCriteriaConsumer(
			CriteriaBuilder criteriaBuilder,
			Root<T> root) {
		this.criteriaBuilder = criteriaBuilder;
		this.predicate = criteriaBuilder.conjunction();
		this.root = root;
	}
	
	@Override
	public void accept(SearchCriteria searchParam) {
		log.info("accept START");
		String operation = searchParam.getOperation();
		Expression<String> expression = root.get(searchParam.getKey());
		String value = searchParam.getValue().toString();
		
		if( operation.equalsIgnoreCase(">") ){
			this.predicate = criteriaBuilder.and(
				predicate, 
				criteriaBuilder.greaterThanOrEqualTo(expression, value)
			);
		} else if( operation.equalsIgnoreCase("<") ) {
			this.predicate = criteriaBuilder.and(
				predicate, 
				criteriaBuilder.lessThanOrEqualTo(expression, value)
			);
		} else {
			if ( expression.getJavaType() == String.class ) {
                predicate = criteriaBuilder.and(
            		predicate, 
            		criteriaBuilder.like(criteriaBuilder.lower(expression), "%" + value.toLowerCase() + "%")
            	);
            } else {
                predicate = criteriaBuilder.and(
                	predicate, 
                	criteriaBuilder.equal(expression, value)
                );
            }
		}
	}
}