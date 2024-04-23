package com.chemista15.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.chemista15.domain.OcrTask} entity. This class is used
 * in {@link com.chemista15.web.rest.OcrTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ocr-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uuid;

    private LongFilter mediaId;

    private IntegerFilter pageNumber;

    private StringFilter jobStatus;

    private ZonedDateTimeFilter createTime;

    private ZonedDateTimeFilter startTime;

    private ZonedDateTimeFilter stopTime;

    private Boolean distinct;

    public OcrTaskCriteria() {}

    public OcrTaskCriteria(OcrTaskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.uuid = other.optionalUuid().map(UUIDFilter::copy).orElse(null);
        this.mediaId = other.optionalMediaId().map(LongFilter::copy).orElse(null);
        this.pageNumber = other.optionalPageNumber().map(IntegerFilter::copy).orElse(null);
        this.jobStatus = other.optionalJobStatus().map(StringFilter::copy).orElse(null);
        this.createTime = other.optionalCreateTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.stopTime = other.optionalStopTime().map(ZonedDateTimeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OcrTaskCriteria copy() {
        return new OcrTaskCriteria(this);
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

    public IntegerFilter getPageNumber() {
        return pageNumber;
    }

    public Optional<IntegerFilter> optionalPageNumber() {
        return Optional.ofNullable(pageNumber);
    }

    public IntegerFilter pageNumber() {
        if (pageNumber == null) {
            setPageNumber(new IntegerFilter());
        }
        return pageNumber;
    }

    public void setPageNumber(IntegerFilter pageNumber) {
        this.pageNumber = pageNumber;
    }

    public StringFilter getJobStatus() {
        return jobStatus;
    }

    public Optional<StringFilter> optionalJobStatus() {
        return Optional.ofNullable(jobStatus);
    }

    public StringFilter jobStatus() {
        if (jobStatus == null) {
            setJobStatus(new StringFilter());
        }
        return jobStatus;
    }

    public void setJobStatus(StringFilter jobStatus) {
        this.jobStatus = jobStatus;
    }

    public ZonedDateTimeFilter getCreateTime() {
        return createTime;
    }

    public Optional<ZonedDateTimeFilter> optionalCreateTime() {
        return Optional.ofNullable(createTime);
    }

    public ZonedDateTimeFilter createTime() {
        if (createTime == null) {
            setCreateTime(new ZonedDateTimeFilter());
        }
        return createTime;
    }

    public void setCreateTime(ZonedDateTimeFilter createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTimeFilter getStartTime() {
        return startTime;
    }

    public Optional<ZonedDateTimeFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public ZonedDateTimeFilter startTime() {
        if (startTime == null) {
            setStartTime(new ZonedDateTimeFilter());
        }
        return startTime;
    }

    public void setStartTime(ZonedDateTimeFilter startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTimeFilter getStopTime() {
        return stopTime;
    }

    public Optional<ZonedDateTimeFilter> optionalStopTime() {
        return Optional.ofNullable(stopTime);
    }

    public ZonedDateTimeFilter stopTime() {
        if (stopTime == null) {
            setStopTime(new ZonedDateTimeFilter());
        }
        return stopTime;
    }

    public void setStopTime(ZonedDateTimeFilter stopTime) {
        this.stopTime = stopTime;
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
        final OcrTaskCriteria that = (OcrTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(jobStatus, that.jobStatus) &&
            Objects.equals(createTime, that.createTime) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(stopTime, that.stopTime) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, mediaId, pageNumber, jobStatus, createTime, startTime, stopTime, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrTaskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUuid().map(f -> "uuid=" + f + ", ").orElse("") +
            optionalMediaId().map(f -> "mediaId=" + f + ", ").orElse("") +
            optionalPageNumber().map(f -> "pageNumber=" + f + ", ").orElse("") +
            optionalJobStatus().map(f -> "jobStatus=" + f + ", ").orElse("") +
            optionalCreateTime().map(f -> "createTime=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalStopTime().map(f -> "stopTime=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
