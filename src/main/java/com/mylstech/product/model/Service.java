package com.mylstech.product.model;

import com.mylstech.product.util.ServiceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "services")
@EqualsAndHashCode(of = "serviceId")
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Column(nullable = false, length = 255)
    private String title;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private ImageEntity image;

    @Column(length = 500)
    private String description;

    @Column(length = 700)
    private String longDescription1;

    @Column(length = 700)
    private String longDescription2;

}

