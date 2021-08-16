package com.api.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
* collection for sequences in tables
* .
*/
@Getter
@Setter
@Accessors(
  chain =true,
  fluent=false
)

@Document(collection = "db_sequences")
public class DBSequence {

    @Id
    private String id;
    private long seq;
    private String collName;
}
