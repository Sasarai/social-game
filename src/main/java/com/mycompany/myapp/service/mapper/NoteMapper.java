package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.NoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Note and its DTO NoteDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, JeuMapper.class, })
public interface NoteMapper extends EntityMapper <NoteDTO, Note> {

    @Mapping(source = "utilisateur.id", target = "utilisateurId")
    @Mapping(source = "utilisateur.login", target = "utilisateurLogin")

    @Mapping(source = "jeu.id", target = "jeuId")
    @Mapping(source = "jeu.nom", target = "jeuNom")
    NoteDTO toDto(Note note); 

    @Mapping(source = "utilisateurId", target = "utilisateur")

    @Mapping(source = "jeuId", target = "jeu")
    Note toEntity(NoteDTO noteDTO); 
    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
