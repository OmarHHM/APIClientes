package com.api;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.api.dto.Customer;
import com.api.repository.CustomerRepository;
import com.api.service.CustomerService;
import com.api.service.impl.CustomerServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
class ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerService customerService= new CustomerServiceImpl();
	
	@Test
	void contextLoads() {
	}

	HttpHeaders headers = new HttpHeaders();
	TestRestTemplate restTemplate = new TestRestTemplate();

	@Test
	public void testGetById404(){
		customerRepository.deleteAll();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Customer> response = restTemplate.exchange(createURLWithPort("/api/v1/customers/1"),
                HttpMethod.GET, entity, Customer.class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testList404(){
		customerRepository.deleteAll();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Customer[]> response = restTemplate.exchange(createURLWithPort("/api/v1/customers"),
                HttpMethod.GET, entity, Customer[].class);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testGetAndList200(){
		createCustomer();		
		createCustomer();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		
		ResponseEntity<Customer[]> response = restTemplate.exchange(createURLWithPort("/api/v1/customers"),
                HttpMethod.GET, entity, Customer[].class);
		assertEquals(2, response.getBody().length);
		
		
		ResponseEntity<Customer> response1 = restTemplate.exchange(createURLWithPort("/api/v1/customers/1"),
                HttpMethod.GET, entity, Customer.class);	
		assertEquals(HttpStatus.OK, response1.getStatusCode());
		
	}
	

	@Test
	public void testListFilters(){		
		Customer customer= new Customer();
		customer.setId(1L);
		customerService.delete(customer);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<Customer[]> response = restTemplate.exchange(
												createURLWithPort("/api/v1/customers?status=A"),
												HttpMethod.GET, entity, Customer[].class);
		assertEquals(1, response.getBody().length);
		
		response = restTemplate.exchange(createURLWithPort("/api/v1/customers?status=I"),
                HttpMethod.GET, entity, Customer[].class);
		assertEquals(1, response.getBody().length);
	}

	
	@Test
	public void testListInvalidFilter(){				
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Customer[]> response = restTemplate.exchange(
											  	createURLWithPort("/api/v1/customers?status=G"),
											  	HttpMethod.GET, entity, Customer[].class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		ResponseEntity<Customer[]> response1 = restTemplate.exchange(
												createURLWithPort("/api/v1/customers?status="),
												HttpMethod.GET, entity, Customer[].class);
		assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

	}
	
	
	@Test
	public void testSave(){			

		String body="{\n"
				+ "    \"firstName\":\"juana\",\n"
				+ "    \"lastName\":\"Hernandez\",\n"
				+ "    \"dateOfBirth\":\"1991-01-30\"\n"
				+ "}";
		
		HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity response = restTemplate.exchange(createURLWithPort("/api/v1/customers"),
                HttpMethod.POST, entity, Customer.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	

	@Test
	public void testUpdate(){			
		String body="{\n"
				+ "    \"id\":1,\n"
				+ "    \"firstName\":\"Lopez\",\n"
				+ "    \"lastName\":\"Hernandez\",\n"
				+ "    \"dateOfBirth\":\"1991-01-30\"\n"
				+ "}";
		
		HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity response = restTemplate.exchange(createURLWithPort("/api/v1/customers"),
                HttpMethod.PUT, entity, Customer.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testUpdateWithoutId(){			
		String body="{\n"
				+ "    \"firstName\":\"Lopez\",\n"
				+ "    \"lastName\":\"Hernandez\",\n"
				+ "    \"dateOfBirth\":\"1991-01-30\"\n"
				+ "}";
		
		HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
		headers.setContentType(MediaType.APPLICATION_JSON);

		ResponseEntity response = restTemplate.exchange(createURLWithPort("/api/v1/customers"),
                HttpMethod.PUT, entity, Customer.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	

	@Test
	public void testDelete(){
		customerRepository.deleteAll();
		createCustomer();
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity response = restTemplate.exchange(createURLWithPort("/api/v1/customers/4"),
                HttpMethod.DELETE, entity, Customer.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		response = restTemplate.exchange(createURLWithPort("/api/v1/customers/5"),
                HttpMethod.DELETE, entity, Customer.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	
	private void createCustomer(){		
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1995-01-01");
			Customer customer= new Customer();
			customer.setFirstName("Cliente1")
					.setLastName("ApellidoCliente1")
					.setDateOfBirth(date);		
			Long id= customerService.create(customer);
			System.out.println("Identifier "+id);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
