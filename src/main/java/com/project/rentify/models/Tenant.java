package com.project.rentify.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int Id;

    String name;
    String email;
    String phone;

    @OneToMany(mappedBy = "tenant")
    List<RentalContracts> contracts;
}
