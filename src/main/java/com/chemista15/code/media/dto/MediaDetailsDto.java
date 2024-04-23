package com.chemista15.code.media.dto;

import com.chemista15.code.pdf.sevice.dto.PdfOutlineTreeNodeDto;
import java.io.Serializable;
import java.util.Objects;

public class MediaDetailsDto implements Serializable {

    private Long id;
    private String fileName;
    private String fileType;
    private String fileDesc;
    private int lastPageNumber;

    private PdfOutlineTreeNodeDto outlines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public Integer getLastPageNumber() {
        return lastPageNumber;
    }

    public void setLastPageNumber(Integer lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MediaDetailsDto)) {
            return false;
        }

        MediaDetailsDto mediaDetailsDTO = (MediaDetailsDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mediaDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MediaDetailsDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", fileDesc='" + getFileDesc() + "'" +
            ", lastPageNumber=" + getLastPageNumber() +
            "}";
    }

    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    public PdfOutlineTreeNodeDto getOutlines() {
        return outlines;
    }

    public void setOutlines(PdfOutlineTreeNodeDto outlines) {
        this.outlines = outlines;
    }
}
