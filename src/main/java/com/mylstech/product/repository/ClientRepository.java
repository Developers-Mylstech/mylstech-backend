package com.mylstech.product.repository;

import com.mylstech.product.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByImageImageUrl(String imageUrl);


}