package com.chemista15.code.pdf.sevice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfOutlineTreeNodeDto implements Serializable {

    private int id;
    private String name;
    private List<PdfOutlineTreeNodeDto> children;
    private PdfOutlineTreeNodeDto parent;
    private int pageNumber;

    public PdfOutlineTreeNodeDto(PdfOutlineTreeNodeDto parent, int id, String name) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.children = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PdfOutlineTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<PdfOutlineTreeNodeDto> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "PdfOutlineTreeNodeDto{" + "id=" + id + ", name='" + name + '\'' + ", children=" + children + '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId() {
        return String.valueOf(id);
    }

    @JsonIgnore
    public PdfOutlineTreeNodeDto getParent() {
        return parent;
    }

    public void setParent(PdfOutlineTreeNodeDto parent) {
        this.parent = parent;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
