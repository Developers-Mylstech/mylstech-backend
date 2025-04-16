package com.mylstech.product.repository;

import com.mylstech.product.model.Service;
import com.mylstech.product.util.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    /**
     * Find all services by service type
     * @param serviceType The service type to filter by
     * @return List of services with the specified service type
     */
    List<Service> findByServiceType(ServiceType serviceType);
}
