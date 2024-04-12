package com.chemista15.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.chemista15.domain.Book} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookDTO implements Serializable {

    private Long id;

    private UUID uuid;

    private String name;

    @Lob
    private String description;

    private Integer mediaStartPage;

    private Integer mediaEndPage;

    private AuthorDTO author;

    private MediaDTO media;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMediaStartPage() {
        return mediaStartPage;
    }

    public void setMediaStartPage(Integer mediaStartPage) {
        this.mediaStartPage = mediaStartPage;
    }

    public Integer getMediaEndPage() {
        return mediaEndPage;
    }

    public void setMediaEndPage(Integer mediaEndPage) {
        this.mediaEndPage = mediaEndPage;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public MediaDTO getMedia() {
        return media;
    }

    public void setMedia(MediaDTO media) {
        this.media = media;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookDTO)) {
            return false;
        }

        BookDTO bookDTO = (BookDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mediaStartPage=" + getMediaStartPage() +
            ", mediaEndPage=" + getMediaEndPage() +
            ", author=" + getAuthor() +
            ", media=" + getMedia() +
            "}";
    }
}
