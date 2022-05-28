package com.authentication.role.permission.based.authentication.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {

    @Id
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "seq_role"
    )
    @SequenceGenerator(
            name = "seq_role",
            allocationSize = 20
    )
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id",
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id",
                    referencedColumnName = "id",
                    nullable = false
            )
    )
    private Collection<Permission> permissions;
}
