package com.authentication.role.permission.based.authentication.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "seq_user"
    )
    @SequenceGenerator(
            name = "seq_user",
            allocationSize = 20
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(
            unique = true,
            nullable = false
    )
    private String username;

    @Column(
            nullable = false
    )
    private String password;

    @Column(nullable = false)
    private String email;

    private String address;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    nullable = false
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id",
                    nullable = false
            )
    )
    private Collection<Role> role = new ArrayList<>();
}
