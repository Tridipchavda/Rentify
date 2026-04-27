package com.project.rentify.models;
import com.project.rentify.models.enums.MaintainanceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class MaintainanceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int Id;
    String description;
    LocalDate RequestDate;
    MaintainanceStatus status;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
}
