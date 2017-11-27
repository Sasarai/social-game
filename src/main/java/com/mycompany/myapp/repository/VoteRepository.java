package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Vote;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Vote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {

    @Query("select vote from Vote vote where vote.user.login = ?#{principal.username}")
    List<Vote> findByUserIsCurrentUser();

    @Query("select vote from Vote vote where vote.evenement.id = :idEvenement")
    List<Vote> findByIdEvenement(@Param("idEvenement") Long idEvenement);

}
