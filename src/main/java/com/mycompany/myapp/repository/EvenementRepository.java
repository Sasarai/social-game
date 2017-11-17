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

    @Query( nativeQuery = true,
        value = "SELECT * FROM evenement WHERE id In ( " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN jhi_user u ON u.id = s.administrateur_id WHERE u.login = :login " +
            "UNION " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIn sphere s ON e.sphere_id = s.id " +
            "INNER JOIN sphere_abonnes sa ON s.id = sa.spheres_id " +
            "INNER JOIN jhi_user u ON sa.abonnes_id = u.id " +
            "WHERE u.login = :login)"
    )
    List<Evenement> findByUtilisateurAyantAcces(@Param("login") String login);

}
