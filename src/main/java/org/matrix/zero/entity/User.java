package org.matrix.zero.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "user_matrix")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy="user", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    private Set<TokenMatrix> tokenMatrixSet;
}
