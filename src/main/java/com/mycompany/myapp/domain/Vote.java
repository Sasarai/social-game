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

/**
 * A Vote.
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "vote")
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nombre_vote")
    private Double nombreVote;

    @OneToMany(mappedBy = "vote")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Jeu> jeus = new HashSet<>();

    @OneToMany(mappedBy = "vote")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Evenement> evenements = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNombreVote() {
        return nombreVote;
    }

    public Vote nombreVote(Double nombreVote) {
        this.nombreVote = nombreVote;
        return this;
    }

    public void setNombreVote(Double nombreVote) {
        this.nombreVote = nombreVote;
    }

    public Set<Jeu> getJeus() {
        return jeus;
    }

    public Vote jeus(Set<Jeu> jeus) {
        this.jeus = jeus;
        return this;
    }

    public Vote addJeu(Jeu jeu) {
        this.jeus.add(jeu);
        jeu.setVote(this);
        return this;
    }

    public Vote removeJeu(Jeu jeu) {
        this.jeus.remove(jeu);
        jeu.setVote(null);
        return this;
    }

    public void setJeus(Set<Jeu> jeus) {
        this.jeus = jeus;
    }

    public Set<Evenement> getEvenements() {
        return evenements;
    }

    public Vote evenements(Set<Evenement> evenements) {
        this.evenements = evenements;
        return this;
    }

    public Vote addEvenement(Evenement evenement) {
        this.evenements.add(evenement);
        evenement.setVote(this);
        return this;
    }

    public Vote removeEvenement(Evenement evenement) {
        this.evenements.remove(evenement);
        evenement.setVote(null);
        return this;
    }

    public void setEvenements(Set<Evenement> evenements) {
        this.evenements = evenements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vote vote = (Vote) o;
        if (vote.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Vote{" +
            "id=" + getId() +
            ", nombreVote='" + getNombreVote() + "'" +
            "}";
    }
}
