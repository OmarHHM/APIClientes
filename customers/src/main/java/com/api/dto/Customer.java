package com.api.dto;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* Domain for a Customer.
*/
@Getter
@Setter
@Accessors(
  chain =true,
  fluent=false
)
@Validated
@Document(collection = "customers")
@ApiModel(value = "Descripcion de DTO")
public class Customer {

	@Id
    private Long id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private Date dateOfBirth;
	@JsonIgnore
	private String status;	
}
