package com.mycompany.myapp.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Vote entity.
 */
public class VoteDTO implements Serializable {

    private Long id;

    private Double nombreVote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNombreVote() {
        return nombreVote;
    }

    public void setNombreVote(Double nombreVote) {
        this.nombreVote = nombreVote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VoteDTO voteDTO = (VoteDTO) o;
        if(voteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VoteDTO{" +
            "id=" + getId() +
            ", nombreVote='" + getNombreVote() + "'" +
            "}";
    }
}
