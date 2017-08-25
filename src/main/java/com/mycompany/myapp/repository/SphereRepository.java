package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sphere;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Sphere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SphereRepository extends JpaRepository<Sphere,Long> {

    @Query("select sphere from Sphere sphere where sphere.administrateur.login = ?#{principal.username}")
    List<Sphere> findByAdministrateurIsCurrentUser();
    
    @Query("select distinct sphere from Sphere sphere left join fetch sphere.abonnes")
    List<Sphere> findAllWithEagerRelationships();

    @Query("select sphere from Sphere sphere left join fetch sphere.abonnes where sphere.id =:id")
    Sphere findOneWithEagerRelationships(@Param("id") Long id);
    
}
