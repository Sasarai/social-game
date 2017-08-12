package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Evenement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Evenement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EvenementRepository extends JpaRepository<Evenement,Long> {
    
    @Query("select distinct evenement from Evenement evenement left join fetch evenement.jeuxes")
    List<Evenement> findAllWithEagerRelationships();

    @Query("select evenement from Evenement evenement left join fetch evenement.jeuxes where evenement.id =:id")
    Evenement findOneWithEagerRelationships(@Param("id") Long id);
    
}
