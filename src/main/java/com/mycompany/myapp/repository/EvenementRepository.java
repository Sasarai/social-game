package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Evenement;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
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

    @Query(
        nativeQuery = true,
        value = "SELECT e.* FROM evenement e " +
            "LEFT JOIN vote v ON e.id = v.evenement_id " +
            "LEFT JOIN jhi_user u ON v.user_id = u.id " +
            "WHERE e.id IN ( " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN jhi_user u ON s.administrateur_id = u.id " +
            "WHERE u.login = ?#{principal.username} " +
            "AND e.date_fin_vote > now() " +
            "UNION " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN sphere_abonnes a ON s.id = a.spheres_id " +
            "INNER JOIN jhi_user u ON a.abonnes_id = u.id " +
            "WHERE u.login = ?#{principal.username} " +
            "AND e.date_fin_vote > now()) " +
            "AND (v.id IS NULL OR u.login != ?#{principal.username})"
    )
    List<Evenement> findEvenementsParUtilisateurNonVotant();

    @Query(
        nativeQuery = true,
        value = "SELECT e.* FROM evenement e " +
            "LEFT JOIN vote v ON e.id = v.evenement_id " +
            "LEFT JOIN jhi_user u ON v.user_id = u.id " +
            "WHERE e.id IN ( " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN jhi_user u ON s.administrateur_id = u.id " +
            "WHERE u.login = ?#{principal.username} " +
            "AND e.date_fin_vote > now() " +
            "UNION " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN sphere_abonnes a ON s.id = a.spheres_id " +
            "INNER JOIN jhi_user u ON a.abonnes_id = u.id " +
            "WHERE u.login = ?#{principal.username} " +
            "AND e.date_fin_vote > now()) " +
            "AND (v.id IS NULL OR u.login != ?#{principal.username})",
        countQuery = "SELECT COUNT (DISTINCT e.*) FROM evenement e " +
            "LEFT JOIN vote v ON e.id = v.evenement_id " +
            "LEFT JOIN jhi_user u ON v.user_id = u.id " +
            "WHERE e.id IN ( " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN jhi_user u ON s.administrateur_id = u.id " +
            "WHERE u.login = 'sasarai' " +
            "AND e.date_fin_vote > now() " +
            "UNION " +
            "SELECT e.id FROM evenement e " +
            "INNER JOIN sphere s ON e.sphere_id = s.id " +
            "INNER JOIN sphere_abonnes a ON s.id = a.spheres_id " +
            "INNER JOIN jhi_user u ON a.abonnes_id = u.id " +
            "WHERE u.login = 'sasarai' " +
            "AND e.date_fin_vote > now()) " +
            "AND (v.id IS NULL OR u.login != 'sasarai')"
    )
    Page<Evenement> findEvenementsParUtilisateurNonVotant(Pageable pageable);

}
