package com.chemista15.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.chemista15.domain.Author} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AuthorDTO implements Serializable {

    private Long id;

    private UUID uuid;

    private String name;

    private String callsign;

    @Lob
    private byte[] image;

    private String imageContentType;

    @Lob
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthorDTO)) {
            return false;
        }

        AuthorDTO authorDTO = (AuthorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, authorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AuthorDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", callsign='" + getCallsign() + "'" +
            ", image='" + getImage() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
