package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.JeuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Jeu and its DTO JeuDTO.
 */
@Mapper(componentModel = "spring", uses = {VoteMapper.class, })
public interface JeuMapper extends EntityMapper <JeuDTO, Jeu> {

    @Mapping(source = "vote.id", target = "voteId")
    JeuDTO toDto(Jeu jeu); 

    @Mapping(source = "voteId", target = "vote")
    Jeu toEntity(JeuDTO jeuDTO); 
    default Jeu fromId(Long id) {
        if (id == null) {
            return null;
        }
        Jeu jeu = new Jeu();
        jeu.setId(id);
        return jeu;
    }
}
