package com.mycompany.myapp.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Note entity.
 */
public class NoteDTO implements Serializable {

    private Long id;

    private Integer note;

    private Long utilisateurId;

    private String utilisateurLogin;

    private Long jeuId;

    private String jeuNom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long userId) {
        this.utilisateurId = userId;
    }

    public String getUtilisateurLogin() {
        return utilisateurLogin;
    }

    public void setUtilisateurLogin(String userLogin) {
        this.utilisateurLogin = userLogin;
    }

    public Long getJeuId() {
        return jeuId;
    }

    public void setJeuId(Long jeuId) {
        this.jeuId = jeuId;
    }

    public String getJeuNom() {
        return jeuNom;
    }

    public void setJeuNom(String jeuNom) {
        this.jeuNom = jeuNom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if(noteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), noteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
