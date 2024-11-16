package org.bits.userservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ROLES")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name= "name")
    private String name;
}