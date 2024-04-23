package com.chemista15.code.media.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

public class PageImageTransferDto implements Serializable {

    private Integer pageNumber;

    @Lob
    private byte[] image;

    private String imageContentType;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageImageTransferDto)) {
            return false;
        }

        PageImageTransferDto pageImageTransferDTO = (PageImageTransferDto) o;
        if (this.pageNumber == null) {
            return false;
        }
        return Objects.equals(this.pageNumber, pageImageTransferDTO.pageNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pageNumber);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PageImageTransferDto{" +
            "pageNumber=" + getPageNumber() +
            ", image='" + getImage() + "'" +
            "}";
    }
}
