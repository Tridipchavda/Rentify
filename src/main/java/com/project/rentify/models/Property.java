package com.project.rentify.models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.rentify.models.enums.PropertyStatus;
import com.project.rentify.models.enums.PropertyType;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String address;
    String city;

    PropertyType type;
    PropertyStatus status;
    int rentAmount;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonManagedReference
    Owner owner;


    @ManyToOne
    @JoinColumn(name = "tenant_id")
    @JsonManagedReference
    Tenant tenant;

}
