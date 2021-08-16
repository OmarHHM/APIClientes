package com.api.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.api.dto.Customer;
import com.api.dto.DBSequence;
import com.api.repository.CustomerRepository;
import com.api.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private MongoOperations mongoOperations;

	@Override
	public Long create(Customer customer) {
		customer.setId(getNextId("customer"));
		customer.setStatus("A"); // Status Active
		return repository.save(customer).getId();
	}

	@Override
	public Long update(Customer customer) {

		return repository.save(customer).getId();
	}

	@Override
	public void delete(Customer customer) {
		customer.setStatus("I"); // Status Inactive
		repository.save(customer);
	}

	@Override
	public Customer get(Long id) {
		Customer cu = repository.findById(id);
		return cu;
	}

	@Override
	public List<Customer> getAll() {
		return repository.findAll();
	}

	private Long getNextId(final String name) {
		final Query q = new Query(Criteria.where("collName").is(name));
		final Update u = new Update().inc("seq", 1);
		final DBSequence counter = mongoOperations.findAndModify(q, u, 
				FindAndModifyOptions.options()
									.returnNew(true)
									.upsert(true), DBSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
}
