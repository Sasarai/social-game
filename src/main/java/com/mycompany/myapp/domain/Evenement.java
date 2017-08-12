package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Evenement.
 */
@Entity
@Table(name = "evenement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "evenement")
public class Evenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "nom")
    private String nom;

    @Column(name = "date_fin_vote")
    private ZonedDateTime dateFinVote;

    @OneToMany(mappedBy = "evenement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sphere> spheres = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "evenement_jeux",
               joinColumns = @JoinColumn(name="evenements_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="jeuxes_id", referencedColumnName="id"))
    private Set<Jeu> jeuxes = new HashSet<>();

    @OneToMany(mappedBy = "evenement")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Vote> votes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Evenement date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public Evenement lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getNom() {
        return nom;
    }

    public Evenement nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ZonedDateTime getDateFinVote() {
        return dateFinVote;
    }

    public Evenement dateFinVote(ZonedDateTime dateFinVote) {
        this.dateFinVote = dateFinVote;
        return this;
    }

    public void setDateFinVote(ZonedDateTime dateFinVote) {
        this.dateFinVote = dateFinVote;
    }

    public Set<Sphere> getSpheres() {
        return spheres;
    }

    public Evenement spheres(Set<Sphere> spheres) {
        this.spheres = spheres;
        return this;
    }

    public Evenement addSphere(Sphere sphere) {
        this.spheres.add(sphere);
        sphere.setEvenement(this);
        return this;
    }

    public Evenement removeSphere(Sphere sphere) {
        this.spheres.remove(sphere);
        sphere.setEvenement(null);
        return this;
    }

    public void setSpheres(Set<Sphere> spheres) {
        this.spheres = spheres;
    }

    public Set<Jeu> getJeuxes() {
        return jeuxes;
    }

    public Evenement jeuxes(Set<Jeu> jeus) {
        this.jeuxes = jeus;
        return this;
    }

    public Evenement addJeux(Jeu jeu) {
        this.jeuxes.add(jeu);
        jeu.getEvenements().add(this);
        return this;
    }

    public Evenement removeJeux(Jeu jeu) {
        this.jeuxes.remove(jeu);
        jeu.getEvenements().remove(this);
        return this;
    }

    public void setJeuxes(Set<Jeu> jeus) {
        this.jeuxes = jeus;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Evenement votes(Set<Vote> votes) {
        this.votes = votes;
        return this;
    }

    public Evenement addVotes(Vote vote) {
        this.votes.add(vote);
        vote.setEvenement(this);
        return this;
    }

    public Evenement removeVotes(Vote vote) {
        this.votes.remove(vote);
        vote.setEvenement(null);
        return this;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Evenement evenement = (Evenement) o;
        if (evenement.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), evenement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Evenement{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", nom='" + getNom() + "'" +
            ", dateFinVote='" + getDateFinVote() + "'" +
            "}";
    }
}
