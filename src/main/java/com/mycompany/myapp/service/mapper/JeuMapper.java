package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.JeuDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Jeu and its DTO JeuDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface JeuMapper extends EntityMapper <JeuDTO, Jeu> {

    @Mapping(source = "proprietaire.id", target = "proprietaireId")
    @Mapping(source = "proprietaire.login", target = "proprietaireLogin")
    JeuDTO toDto(Jeu jeu);

    @Named("WithoutImageMapper")
    @Mapping(source = "proprietaire.id", target = "proprietaireId")
    @Mapping(source = "proprietaire.login", target = "proprietaireLogin")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "imageContentType", ignore = true)
    JeuDTO toDtoWithoutImage(Jeu jeu);

    @Mapping(target = "evenements", ignore = true)
    @Mapping(target = "votes", ignore = true)
    @Mapping(source = "proprietaireId", target = "proprietaire")
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
