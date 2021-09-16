package org.starter.springboot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "token_matrix")
public class TokenMatrix implements Serializable {

    @Id
    private String id;

    private String token;

    private String userId;
}
