package com.mycompany.myapp.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Evenement entity.
 */
public class EvenementDTO implements Serializable {

    private Long id;

    private ZonedDateTime date;

    private String lieu;

    private String nom;

    private ZonedDateTime dateFinVote;

    private Set<JeuDTO> jeuxes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ZonedDateTime getDateFinVote() {
        return dateFinVote;
    }

    public void setDateFinVote(ZonedDateTime dateFinVote) {
        this.dateFinVote = dateFinVote;
    }

    public Set<JeuDTO> getJeuxes() {
        return jeuxes;
    }

    public void setJeuxes(Set<JeuDTO> jeus) {
        this.jeuxes = jeus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EvenementDTO evenementDTO = (EvenementDTO) o;
        if(evenementDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), evenementDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EvenementDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateFinVote='" + getDateFinVote() + "'" +
            "}";
    }
}
