package com.mylstech.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppSetting {
    @Id
    @Column(name = "setting_key", length = 100)  // Rename to avoid reserved keyword
    private String key;
    
    @Column(columnDefinition = "TEXT")
    private String value;
    
    @Column(length = 255)
    private String description;
}