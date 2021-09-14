package org.starter.springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "user")
public class User {

    @Id
    private String id;

    private String email;

    private String firstName;

    private String lastName;
}
