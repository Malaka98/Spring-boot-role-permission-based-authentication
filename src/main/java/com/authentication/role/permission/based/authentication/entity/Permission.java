package com.authentication.role.permission.based.authentication.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Permission {

    @Id
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "seq_permission"
    )
    @SequenceGenerator(
            name = "seq_permission",
            allocationSize = 20
    )
    private Long id;

    @Column(
            name = "access_permission",
            nullable = false,
            unique = true
    )
    private String accessPermission;
}
