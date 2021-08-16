package com.api.controllers;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.Customer;
import com.api.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/v1/customers")
@Api(tags = "API para gestionar clientes")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
				value = "Metodo para crear un cliente.",
			    notes = "Nos permite crear un cliente")
	
	@ApiResponses(value = {
		    @ApiResponse(code = 201, message = "Cliente creado correctamente"),
		    @ApiResponse(code = 400, message = "Peticion incorrecta"),
		    @ApiResponse(code = 500, message = "Error interno de servidor")}
		)
	public ResponseEntity<String> create(@Valid @RequestBody Customer customer) {
		customerService.create(customer);
		URI location = URI.create("/api/v1/customers/"+customer.getId());
		return ResponseEntity.created(location).build();
	}
	
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(
			value = "Metodo para modificar un cliente en especifico.",
		    notes = "Nos permite modificar un cliente en especifico")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Cliente modificado correctamente"),
		    @ApiResponse(code = 400, message = "Peticion incorrecta"),
		    @ApiResponse(code = 500, message = "Error interno de servidor")}
		)
	public ResponseEntity update(@RequestBody Customer customer) {
		
		if(null==customer.getId() || 0==customer.getId()) {
			return ResponseEntity.badRequest().build();
		}else {
			customerService.update(customer);
		}
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping(path="/{id}")
	@ApiOperation(
			value = "Metodo para eliminar un cliente.",
		    notes = "Nos permite eliminar de manera logica un cliente mediante su id")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Cliente eliminado correctamente"),
		    @ApiResponse(code = 400, message = "Peticion incorrecta"),
		    @ApiResponse(code = 500, message = "Error interno de servidor")}
		)
	public ResponseEntity delete(@PathVariable final Long id) {		
		Customer customer= customerService.get(id);
		if(null==customer)
			return ResponseEntity.notFound().build();		
		customerService.delete(customer);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(path="/{id}",produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	@ApiOperation(
			value = "Metodo para obtener un cliente.",
		    notes = "Nos permite consultar un cliente filtrando por su id")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Consulta exitosa"),
		    @ApiResponse(code = 400, message = "Peticion incorrecta"),
		    @ApiResponse(code = 500, message = "Error interno de servidor")}
		)
	public ResponseEntity<Customer> get(@PathVariable final Long id) {
		Customer cu= customerService.get(id);		
		
		if(cu==null || "I".equals(cu.getStatus()))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(cu,HttpStatus.OK);
	}	
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(
			value = "Metodo para listar todos los clientes.",
		    notes = "Nos permite listar todos los clientes, podemos filtrar por status")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Consulta exitosa"),
		    @ApiResponse(code = 400, message = "Peticion incorrecta"),
		    @ApiResponse(code = 500, message = "Error interno de servidor")}
		)
	
	public ResponseEntity<List<Customer>> list(
			@ApiParam(value="Estatus, valores válidos"
					       +"A = Activo   I = Inactivo", name="status")
			@RequestParam(name = "status") Optional<String> status) {
		
		List<Customer> customers= customerService.getAll();	
		
		if( status.isPresent() && (!status.get().equals("I") && !status.get().equals("A"))) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}else if(status.isPresent()) {
			customers = customers.stream().filter(cu->cu.getStatus().equals(status.get())).collect(toList());	
		}
			
		if(customers.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	
		return new ResponseEntity<>(customers,HttpStatus.OK);
	}	
	
}
