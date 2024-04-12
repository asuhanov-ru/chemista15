package com.chemista15.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_desc")
    private String fileDesc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "media" }, allowSetters = true)
    private Collection collection;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "media")
    @JsonIgnoreProperties(value = { "author", "media" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Media id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Media uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Media fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public Media fileType(String fileType) {
        this.setFileType(fileType);
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileDesc() {
        return this.fileDesc;
    }

    public Media fileDesc(String fileDesc) {
        this.setFileDesc(fileDesc);
        return this;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public Collection getCollection() {
        return this.collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Media collection(Collection collection) {
        this.setCollection(collection);
        return this;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Book> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.setMedia(null));
        }
        if (books != null) {
            books.forEach(i -> i.setMedia(this));
        }
        this.books = books;
    }

    public Media books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public Media addBook(Book book) {
        this.books.add(book);
        book.setMedia(this);
        return this;
    }

    public Media removeBook(Book book) {
        this.books.remove(book);
        book.setMedia(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return getId() != null && getId().equals(((Media) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Media{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", fileDesc='" + getFileDesc() + "'" +
            "}";
    }
}
