package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Note;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {

    @Query("select note from Note note where note.utilisateur.login = ?#{principal.username}")
    List<Note> findByUtilisateurIsCurrentUser();
    
}
