package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.VoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vote and its DTO VoteDTO.
 */
@Mapper(componentModel = "spring", uses = {EvenementMapper.class, UserMapper.class, JeuMapper.class, })
public interface VoteMapper extends EntityMapper <VoteDTO, Vote> {

    @Mapping(source = "evenement.id", target = "evenementId")
    @Mapping(source = "evenement.nom", target = "evenementNom")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")

    @Mapping(source = "jeu.id", target = "jeuId")
    @Mapping(source = "jeu.nom", target = "jeuNom")
    VoteDTO toDto(Vote vote); 

    @Mapping(source = "evenementId", target = "evenement")

    @Mapping(source = "userId", target = "user")

    @Mapping(source = "jeuId", target = "jeu")
    Vote toEntity(VoteDTO voteDTO); 
    default Vote fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vote vote = new Vote();
        vote.setId(id);
        return vote;
    }
}
