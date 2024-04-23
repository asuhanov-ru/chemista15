package com.chemista15.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.chemista15.domain.OcrTask} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrTaskDTO implements Serializable {

    private Long id;

    private UUID uuid;

    private Long mediaId;

    private Integer pageNumber;

    private String jobStatus;

    private ZonedDateTime createTime;

    private ZonedDateTime startTime;

    private ZonedDateTime stopTime;

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

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(ZonedDateTime stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrTaskDTO)) {
            return false;
        }

        OcrTaskDTO ocrTaskDTO = (OcrTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ocrTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrTaskDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", mediaId=" + getMediaId() +
            ", pageNumber=" + getPageNumber() +
            ", jobStatus='" + getJobStatus() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", stopTime='" + getStopTime() + "'" +
            "}";
    }
}
