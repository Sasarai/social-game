package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.GenreJeu;

/**
 * A Jeu.
 */
@Entity
@Table(name = "jeu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "jeu")
public class Jeu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "nombre_joueur_min")
    private Double nombreJoueurMin;

    @Column(name = "nombre_joueur_max")
    private Double nombreJoueurMax;

    @Column(name = "duree_moyenne")
    private Double dureeMoyenne;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private GenreJeu genre;

    @ManyToMany(mappedBy = "jeuxes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Evenement> evenements = new HashSet<>();

    @OneToMany(mappedBy = "jeux")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vote> votes = new HashSet<>();

    @ManyToOne
    private User proprietaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Jeu nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getNombreJoueurMin() {
        return nombreJoueurMin;
    }

    public Jeu nombreJoueurMin(Double nombreJoueurMin) {
        this.nombreJoueurMin = nombreJoueurMin;
        return this;
    }

    public void setNombreJoueurMin(Double nombreJoueurMin) {
        this.nombreJoueurMin = nombreJoueurMin;
    }

    public Double getNombreJoueurMax() {
        return nombreJoueurMax;
    }

    public Jeu nombreJoueurMax(Double nombreJoueurMax) {
        this.nombreJoueurMax = nombreJoueurMax;
        return this;
    }

    public void setNombreJoueurMax(Double nombreJoueurMax) {
        this.nombreJoueurMax = nombreJoueurMax;
    }

    public Double getDureeMoyenne() {
        return dureeMoyenne;
    }

    public Jeu dureeMoyenne(Double dureeMoyenne) {
        this.dureeMoyenne = dureeMoyenne;
        return this;
    }

    public void setDureeMoyenne(Double dureeMoyenne) {
        this.dureeMoyenne = dureeMoyenne;
    }

    public String getDescription() {
        return description;
    }

    public Jeu description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public Jeu image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public Jeu imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public GenreJeu getGenre() {
        return genre;
    }

    public Jeu genre(GenreJeu genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(GenreJeu genre) {
        this.genre = genre;
    }

    public Set<Evenement> getEvenements() {
        return evenements;
    }

    public Jeu evenements(Set<Evenement> evenements) {
        this.evenements = evenements;
        return this;
    }

    public Jeu addEvenements(Evenement evenement) {
        this.evenements.add(evenement);
        evenement.getJeuxes().add(this);
        return this;
    }

    public Jeu removeEvenements(Evenement evenement) {
        this.evenements.remove(evenement);
        evenement.getJeuxes().remove(this);
        return this;
    }

    public void setEvenements(Set<Evenement> evenements) {
        this.evenements = evenements;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Jeu votes(Set<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public Jeu addVotes(Vote vote) {
        this.votes.add(vote);
        vote.setJeu(this);
        return this;
    }

    public Jeu removeVotes(Vote vote) {
        this.votes.remove(vote);
        vote.setJeu(null);
        return this;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public User getProprietaire() {
        return proprietaire;
    }

    public Jeu proprietaire(User user) {
        this.proprietaire = user;
        return this;
    }

    public void setProprietaire(User user) {
        this.proprietaire = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jeu jeu = (Jeu) o;
        if (jeu.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), jeu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Jeu{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", nombreJoueurMin='" + getNombreJoueurMin() + "'" +
            ", nombreJoueurMax='" + getNombreJoueurMax() + "'" +
            ", dureeMoyenne='" + getDureeMoyenne() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + imageContentType + "'" +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
