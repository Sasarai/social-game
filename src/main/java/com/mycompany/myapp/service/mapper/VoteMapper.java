package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.VoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Vote and its DTO VoteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VoteMapper extends EntityMapper <VoteDTO, Vote> {
    
    @Mapping(target = "jeus", ignore = true)
    @Mapping(target = "evenements", ignore = true)
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
