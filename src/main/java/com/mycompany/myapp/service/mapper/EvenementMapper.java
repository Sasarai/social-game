package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EvenementDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Evenement and its DTO EvenementDTO.
 */
@Mapper(componentModel = "spring", uses = {JeuMapper.class, SphereMapper.class, })
public interface EvenementMapper extends EntityMapper <EvenementDTO, Evenement> {

    @Mapping(source = "sphere.id", target = "sphereId")
    @Mapping(source = "sphere.nom", target = "sphereNom")
    @Mapping(target = "jeuxes", qualifiedByName = {"WithoutImageMapper"})
    EvenementDTO toDto(Evenement evenement);

    @Mapping(target = "votes", ignore = true)
    @Mapping(source = "sphereId", target = "sphere")
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
