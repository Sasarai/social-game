package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Sphere.
 */
@Entity
@Table(name = "sphere")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "sphere")
public class Sphere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @ManyToOne
    private Evenement evenement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Sphere nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public Sphere evenement(Evenement evenement) {
        this.evenement = evenement;
        return this;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sphere sphere = (Sphere) o;
        if (sphere.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sphere.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sphere{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            "}";
    }
}
