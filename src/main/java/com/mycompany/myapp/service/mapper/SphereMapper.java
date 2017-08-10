package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SphereDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sphere and its DTO SphereDTO.
 */
@Mapper(componentModel = "spring", uses = {EvenementMapper.class, })
public interface SphereMapper extends EntityMapper <SphereDTO, Sphere> {

    @Mapping(source = "evenement.id", target = "evenementId")
    SphereDTO toDto(Sphere sphere); 

    @Mapping(source = "evenementId", target = "evenement")
    Sphere toEntity(SphereDTO sphereDTO); 
    default Sphere fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sphere sphere = new Sphere();
        sphere.setId(id);
        return sphere;
    }
}
