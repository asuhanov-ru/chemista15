package com.chemista15.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.chemista15.domain.Book} entity. This class is used
 * in {@link com.chemista15.web.rest.BookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private StringFilter name;

    private IntegerFilter mediaStartPage;

    private IntegerFilter mediaEndPage;

    private LongFilter authorId;

    private LongFilter mediaId;

    private Boolean distinct;

    public BookCriteria() {}

    public BookCriteria(BookCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.mediaStartPage = other.optionalMediaStartPage().map(IntegerFilter::copy).orElse(null);
        this.mediaEndPage = other.optionalMediaEndPage().map(IntegerFilter::copy).orElse(null);
        this.authorId = other.optionalAuthorId().map(LongFilter::copy).orElse(null);
        this.mediaId = other.optionalMediaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BookCriteria copy() {
        return new BookCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getMediaStartPage() {
        return mediaStartPage;
    }

    public Optional<IntegerFilter> optionalMediaStartPage() {
        return Optional.ofNullable(mediaStartPage);
    }

    public IntegerFilter mediaStartPage() {
        if (mediaStartPage == null) {
            setMediaStartPage(new IntegerFilter());
        }
        return mediaStartPage;
    }

    public void setMediaStartPage(IntegerFilter mediaStartPage) {
        this.mediaStartPage = mediaStartPage;
    }

    public IntegerFilter getMediaEndPage() {
        return mediaEndPage;
    }

    public Optional<IntegerFilter> optionalMediaEndPage() {
        return Optional.ofNullable(mediaEndPage);
    }

    public IntegerFilter mediaEndPage() {
        if (mediaEndPage == null) {
            setMediaEndPage(new IntegerFilter());
        }
        return mediaEndPage;
    }

    public void setMediaEndPage(IntegerFilter mediaEndPage) {
        this.mediaEndPage = mediaEndPage;
    }

    public LongFilter getAuthorId() {
        return authorId;
    }

    public Optional<LongFilter> optionalAuthorId() {
        return Optional.ofNullable(authorId);
    }

    public LongFilter authorId() {
        if (authorId == null) {
            setAuthorId(new LongFilter());
        }
        return authorId;
    }

    public void setAuthorId(LongFilter authorId) {
        this.authorId = authorId;
    }

    public LongFilter getMediaId() {
        return mediaId;
    }

    public Optional<LongFilter> optionalMediaId() {
        return Optional.ofNullable(mediaId);
    }

    public LongFilter mediaId() {
        if (mediaId == null) {
            setMediaId(new LongFilter());
        }
        return mediaId;
    }

    public void setMediaId(LongFilter mediaId) {
        this.mediaId = mediaId;
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
        final BookCriteria that = (BookCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(name, that.name) &&
            Objects.equals(mediaStartPage, that.mediaStartPage) &&
            Objects.equals(mediaEndPage, that.mediaEndPage) &&
            Objects.equals(authorId, that.authorId) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, name, mediaStartPage, mediaEndPage, authorId, mediaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalMediaStartPage().map(f -> "mediaStartPage=" + f + ", ").orElse("") +
            optionalMediaEndPage().map(f -> "mediaEndPage=" + f + ", ").orElse("") +
            optionalAuthorId().map(f -> "authorId=" + f + ", ").orElse("") +
            optionalMediaId().map(f -> "mediaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
