package com.chemista15.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.chemista15.domain.Media} entity. This class is used
 * in {@link com.chemista15.web.rest.MediaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /media?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MediaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter fileName;

    private StringFilter fileType;

    private StringFilter fileDesc;

    private LongFilter collectionId;

    private LongFilter bookId;

    private Boolean distinct;

    public MediaCriteria() {}

    public MediaCriteria(MediaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.fileName = other.optionalFileName().map(StringFilter::copy).orElse(null);
        this.fileType = other.optionalFileType().map(StringFilter::copy).orElse(null);
        this.fileDesc = other.optionalFileDesc().map(StringFilter::copy).orElse(null);
        this.collectionId = other.optionalCollectionId().map(LongFilter::copy).orElse(null);
        this.bookId = other.optionalBookId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MediaCriteria copy() {
        return new MediaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUuid() {
        return uuid;
    }

    public Optional<UUIDFilter> optionalUuid() {
        return Optional.ofNullable(uuid);
    }

    public UUIDFilter uuid() {
        if (uuid == null) {
            setUuid(new UUIDFilter());
        }
        return uuid;
    }

    public void setUuid(UUIDFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public Optional<StringFilter> optionalFileName() {
        return Optional.ofNullable(fileName);
    }

    public StringFilter fileName() {
        if (fileName == null) {
            setFileName(new StringFilter());
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getFileType() {
        return fileType;
    }

    public Optional<StringFilter> optionalFileType() {
        return Optional.ofNullable(fileType);
    }

    public StringFilter fileType() {
        if (fileType == null) {
            setFileType(new StringFilter());
        }
        return fileType;
    }

    public void setFileType(StringFilter fileType) {
        this.fileType = fileType;
    }

    public StringFilter getFileDesc() {
        return fileDesc;
    }

    public Optional<StringFilter> optionalFileDesc() {
        return Optional.ofNullable(fileDesc);
    }

    public StringFilter fileDesc() {
        if (fileDesc == null) {
            setFileDesc(new StringFilter());
        }
        return fileDesc;
    }

    public void setFileDesc(StringFilter fileDesc) {
        this.fileDesc = fileDesc;
    }

    public LongFilter getCollectionId() {
        return collectionId;
    }

    public Optional<LongFilter> optionalCollectionId() {
        return Optional.ofNullable(collectionId);
    }

    public LongFilter collectionId() {
        if (collectionId == null) {
            setCollectionId(new LongFilter());
        }
        return collectionId;
    }

    public void setCollectionId(LongFilter collectionId) {
        this.collectionId = collectionId;
    }

    public LongFilter getBookId() {
        return bookId;
    }

    public Optional<LongFilter> optionalBookId() {
        return Optional.ofNullable(bookId);
    }

    public LongFilter bookId() {
        if (bookId == null) {
            setBookId(new LongFilter());
        }
        return bookId;
    }

    public void setBookId(LongFilter bookId) {
        this.bookId = bookId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MediaCriteria that = (MediaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(fileType, that.fileType) &&
            Objects.equals(fileDesc, that.fileDesc) &&
            Objects.equals(collectionId, that.collectionId) &&
            Objects.equals(bookId, that.bookId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, fileName, fileType, fileDesc, collectionId, bookId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalFileName().map(f -> "fileName=" + f + ", ").orElse("") +
            optionalFileType().map(f -> "fileType=" + f + ", ").orElse("") +
            optionalFileDesc().map(f -> "fileDesc=" + f + ", ").orElse("") +
            optionalCollectionId().map(f -> "collectionId=" + f + ", ").orElse("") +
            optionalBookId().map(f -> "bookId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
