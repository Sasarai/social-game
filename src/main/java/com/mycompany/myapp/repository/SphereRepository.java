package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sphere;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sphere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SphereRepository extends JpaRepository<Sphere,Long> {
    
}
