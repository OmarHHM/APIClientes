package com.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.api.dto.Customer;


public interface CustomerRepository extends MongoRepository<Customer, String> {

	Customer findById(Long customerId);
	
}
