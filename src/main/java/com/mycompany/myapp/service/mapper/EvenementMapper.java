package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EvenementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Evenement and its DTO EvenementDTO.
 */
@Mapper(componentModel = "spring", uses = {VoteMapper.class, })
public interface EvenementMapper extends EntityMapper <EvenementDTO, Evenement> {

    @Mapping(source = "vote.id", target = "voteId")
    EvenementDTO toDto(Evenement evenement); 
    @Mapping(target = "spheres", ignore = true)

    @Mapping(source = "voteId", target = "vote")
    Evenement toEntity(EvenementDTO evenementDTO); 
    default Evenement fromId(Long id) {
        if (id == null) {
            return null;
        }
        Evenement evenement = new Evenement();
        evenement.setId(id);
        return evenement;
    }
}
