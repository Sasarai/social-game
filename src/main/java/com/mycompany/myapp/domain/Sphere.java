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
    private User administrateur;

    @OneToMany(mappedBy = "sphere")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Evenement> evenements = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "sphere_abonnes",
               joinColumns = @JoinColumn(name="spheres_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="abonnes_id", referencedColumnName="id"))
    private Set<User> abonnes = new HashSet<>();

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

    public User getAdministrateur() {
        return administrateur;
    }

    public Sphere administrateur(User user) {
        this.administrateur = user;
        return this;
    }

    public void setAdministrateur(User user) {
        this.administrateur = user;
    }

    public Set<Evenement> getEvenements() {
        return evenements;
    }

    public Sphere evenements(Set<Evenement> evenements) {
        this.evenements = evenements;
        return this;
    }

    public Sphere addEvenements(Evenement evenement) {
        this.evenements.add(evenement);
        evenement.setSphere(this);
        return this;
    }

    public Sphere removeEvenements(Evenement evenement) {
        this.evenements.remove(evenement);
        evenement.setSphere(null);
        return this;
    }

    public void setEvenements(Set<Evenement> evenements) {
        this.evenements = evenements;
    }

    public Set<User> getAbonnes() {
        return abonnes;
    }

    public Sphere abonnes(Set<User> users) {
        this.abonnes = users;
        return this;
    }

    public Sphere addAbonnes(User user) {
        this.abonnes.add(user);
        return this;
    }

    public Sphere removeAbonnes(User user) {
        this.abonnes.remove(user);
        return this;
    }

    public void setAbonnes(Set<User> users) {
        this.abonnes = users;
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
