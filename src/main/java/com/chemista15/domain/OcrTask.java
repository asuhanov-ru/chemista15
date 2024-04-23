package com.chemista15.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * A OcrTask.
 */
@Entity
@Table(name = "ocr_task")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OcrTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "media_id")
    private Long mediaId;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "job_status")
    private String jobStatus;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "stop_time")
    private ZonedDateTime stopTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OcrTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public OcrTask uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getMediaId() {
        return this.mediaId;
    }

    public OcrTask mediaId(Long mediaId) {
        this.setMediaId(mediaId);
        return this;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getPageNumber() {
        return this.pageNumber;
    }

    public OcrTask pageNumber(Integer pageNumber) {
        this.setPageNumber(pageNumber);
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getJobStatus() {
        return this.jobStatus;
    }

    public OcrTask jobStatus(String jobStatus) {
        this.setJobStatus(jobStatus);
        return this;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public ZonedDateTime getCreateTime() {
        return this.createTime;
    }

    public OcrTask createTime(ZonedDateTime createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public OcrTask startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getStopTime() {
        return this.stopTime;
    }

    public OcrTask stopTime(ZonedDateTime stopTime) {
        this.setStopTime(stopTime);
        return this;
    }

    public void setStopTime(ZonedDateTime stopTime) {
        this.stopTime = stopTime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OcrTask)) {
            return false;
        }
        return getId() != null && getId().equals(((OcrTask) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OcrTask{" +
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
