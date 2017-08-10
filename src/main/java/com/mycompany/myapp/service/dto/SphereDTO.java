package com.mycompany.myapp.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Sphere entity.
 */
public class SphereDTO implements Serializable {

    private Long id;

    private String nom;

    private Long evenementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(Long evenementId) {
        this.evenementId = evenementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SphereDTO sphereDTO = (SphereDTO) o;
        if(sphereDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sphereDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SphereDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
