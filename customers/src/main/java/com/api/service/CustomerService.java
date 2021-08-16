package com.api.service;

import java.util.List;

import com.api.dto.Customer;

public interface CustomerService {

	public Long create(Customer customer);
	public Long update(Customer customer);
	public void delete(Customer customer);
	public Customer get(Long id);
	public List<Customer> getAll();
	
}
