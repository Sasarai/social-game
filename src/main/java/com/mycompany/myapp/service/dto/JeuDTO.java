package com.mycompany.myapp.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;
import com.mycompany.myapp.domain.enumeration.GenreJeu;

/**
 * A DTO for the Jeu entity.
 */
public class JeuDTO implements Serializable {

    private Long id;

    private String nom;

    private Double nombreJoueurMin;

    private Double nombreJoueurMax;

    private Double dureeMoyenne;

    private String description;

    @Lob
    private byte[] image;
    private String imageContentType;

    private GenreJeu genre;

    private Long proprietaireId;
    private String proprietaireLogin;

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

    public Double getNombreJoueurMin() {
        return nombreJoueurMin;
    }

    public void setNombreJoueurMin(Double nombreJoueurMin) {
        this.nombreJoueurMin = nombreJoueurMin;
    }

    public Double getNombreJoueurMax() {
        return nombreJoueurMax;
    }

    public void setNombreJoueurMax(Double nombreJoueurMax) {
        this.nombreJoueurMax = nombreJoueurMax;
    }

    public Double getDureeMoyenne() {
        return dureeMoyenne;
    }

    public void setDureeMoyenne(Double dureeMoyenne) {
        this.dureeMoyenne = dureeMoyenne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public GenreJeu getGenre() {
        return genre;
    }

    public void setGenre(GenreJeu genre) {
        this.genre = genre;
    }

    public Long getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(Long userId) {
        this.proprietaireId = userId;
    }

    public String getProprietaireLogin() {
        return proprietaireLogin;
    }

    public void setProprietaireLogin(String userLogin) {
        this.proprietaireLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JeuDTO jeuDTO = (JeuDTO) o;
        if(jeuDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jeuDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JeuDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", nombreJoueurMin='" + getNombreJoueurMin() + "'" +
            ", nombreJoueurMax='" + getNombreJoueurMax() + "'" +
            ", dureeMoyenne='" + getDureeMoyenne() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
