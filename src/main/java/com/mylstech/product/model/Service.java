package com.mylstech.product.model;

import com.mylstech.product.util.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "services")
@EqualsAndHashCode(of = "serviceId")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Embedded
    private Description description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "service_id")
    @ToString.Exclude
    private List<Highlight> highlights = new ArrayList<>();
}