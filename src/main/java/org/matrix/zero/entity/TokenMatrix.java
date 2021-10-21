package org.matrix.zero.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "user"})
@ToString(exclude = { "user"})
@Accessors(chain = true)
@Entity
@Table(name = "token_matrix")
public class TokenMatrix extends AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    private User user;
}
