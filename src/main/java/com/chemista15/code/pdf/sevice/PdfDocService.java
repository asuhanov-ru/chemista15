package com.chemista15.code.pdf.sevice;

import com.chemista15.code.media.dto.MediaDetailsDto;
import com.chemista15.code.media.dto.PageImageTransferDto;
import com.chemista15.code.media.mapper.MediaDetailsMapper;
import com.chemista15.code.pdf.sevice.dto.PdfOutlineTreeNodeDto;
import com.chemista15.domain.Media;
import com.chemista15.repository.MediaRepository;
import com.itextpdf.commons.utils.MessageFormatUtil;
import com.itextpdf.kernel.exceptions.KernelExceptionMessageConstant;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PdfDocService {

    private final Logger log = LoggerFactory.getLogger(PdfDocService.class);

    @Value("${collections.location}")
    private String SRC;

    private final MediaRepository mediaRepository;
    private final MediaDetailsMapper mediaDetailsMapper;

    public PdfDocService(MediaRepository mediaRepository, MediaDetailsMapper mediaDetailsMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaDetailsMapper = mediaDetailsMapper;
    }

    private void addOutlineToPage(PdfOutlineTreeNodeDto outline, PdfDictionary item, PdfDocument doc) {
        outline.setPageNumber(-1);
        PdfObject outlineDestination = item.get(PdfName.Dest);
        if (outlineDestination != null) {
            PdfDestination destination = PdfDestination.makeDestination(outlineDestination);
            PdfDictionary pageObj = (PdfDictionary) destination.getDestinationPage(doc.getCatalog().getNameTree(PdfName.Dests));

            if (pageObj != null) outline.setPageNumber(doc.getPageNumber(pageObj));
        }
    }

    public PdfOutlineTreeNodeDto getOutline(Long mediaId) throws Exception {
        Optional<Media> media = mediaRepository.findOneWithEagerRelationships(mediaId);
        String fileName = media.map(el -> el.getFileName()).orElse("");
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC + fileName));

        PdfDictionary outlineRoot = srcDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.Outlines);
        PdfDictionary current = outlineRoot.getAsDictionary(PdfName.First);

        PdfOutlineTreeNodeDto outlines = new PdfOutlineTreeNodeDto(null, outlineRoot.getIndirectReference().getObjNumber(), "ROOT");
        PdfOutlineTreeNodeDto parentOutline = outlines;

        Map<PdfOutlineTreeNodeDto, PdfDictionary> nextUnprocessedChildForParentMap = new HashMap<>();
        Set<PdfDictionary> alreadyVisitedOutlinesSet = new HashSet<>();

        while (current != null) {
            PdfDictionary parent = current.getAsDictionary(PdfName.Parent);

            if (null == parent) {
                throw new PdfException(
                    MessageFormatUtil.format(
                        KernelExceptionMessageConstant.CORRUPTED_OUTLINE_NO_PARENT_ENTRY,
                        current.getIndirectReference()
                    )
                );
            }

            PdfString title = current.getAsString(PdfName.Title);
            if (null == title) {
                throw new PdfException(
                    MessageFormatUtil.format(
                        KernelExceptionMessageConstant.CORRUPTED_OUTLINE_NO_TITLE_ENTRY,
                        current.getIndirectReference()
                    )
                );
            }

            PdfOutlineTreeNodeDto currentOutline = new PdfOutlineTreeNodeDto(
                parentOutline,
                current.getIndirectReference().getObjNumber(),
                title.toUnicodeString()
            );

            alreadyVisitedOutlinesSet.add(current);
            addOutlineToPage(currentOutline, current, srcDoc);
            if (parentOutline.getChildren() == null) parentOutline.setChildren(new ArrayList<PdfOutlineTreeNodeDto>());
            parentOutline.getChildren().add(currentOutline);

            PdfDictionary first = current.getAsDictionary(PdfName.First);
            PdfDictionary next = current.getAsDictionary(PdfName.Next);

            if (first != null) {
                if (alreadyVisitedOutlinesSet.contains(first)) {
                    throw new PdfException(
                        MessageFormatUtil.format(KernelExceptionMessageConstant.CORRUPTED_OUTLINE_DICTIONARY_HAS_INFINITE_LOOP, first)
                    );
                }
                // Down in hierarchy; when returning up, process `next`.
                nextUnprocessedChildForParentMap.put(parentOutline, next);
                parentOutline = currentOutline;
                current = first;
            } else if (next != null) {
                if (alreadyVisitedOutlinesSet.contains(next)) {
                    throw new PdfException(
                        MessageFormatUtil.format(KernelExceptionMessageConstant.CORRUPTED_OUTLINE_DICTIONARY_HAS_INFINITE_LOOP, next)
                    );
                }
                // Next sibling in hierarchy
                current = next;
            } else {
                // Up in hierarchy using 'nextUnprocessedChildForParentMap'.
                current = null;
                while (current == null && parentOutline != null) {
                    parentOutline = parentOutline.getParent();
                    if (parentOutline != null) {
                        current = nextUnprocessedChildForParentMap.get(parentOutline);
                    }
                }
            }
        }

        srcDoc.close();
        return (outlines);
    }

    private PdfOutlineTreeNodeDto getOutline(PdfDocument srcDoc) throws Exception {
        PdfDictionary outlineRoot = srcDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.Outlines);
        PdfDictionary current = outlineRoot.getAsDictionary(PdfName.First);

        PdfOutlineTreeNodeDto outlines = new PdfOutlineTreeNodeDto(null, outlineRoot.getIndirectReference().getObjNumber(), "ROOT");
        PdfOutlineTreeNodeDto parentOutline = outlines;

        Map<PdfOutlineTreeNodeDto, PdfDictionary> nextUnprocessedChildForParentMap = new HashMap<>();
        Set<PdfDictionary> alreadyVisitedOutlinesSet = new HashSet<>();

        while (current != null) {
            PdfDictionary parent = current.getAsDictionary(PdfName.Parent);

            if (null == parent) {
                throw new PdfException(
                    MessageFormatUtil.format(
                        KernelExceptionMessageConstant.CORRUPTED_OUTLINE_NO_PARENT_ENTRY,
                        current.getIndirectReference()
                    )
                );
            }

            PdfString title = current.getAsString(PdfName.Title);
            if (null == title) {
                throw new PdfException(
                    MessageFormatUtil.format(
                        KernelExceptionMessageConstant.CORRUPTED_OUTLINE_NO_TITLE_ENTRY,
                        current.getIndirectReference()
                    )
                );
            }

            PdfOutlineTreeNodeDto currentOutline = new PdfOutlineTreeNodeDto(
                parentOutline,
                current.getIndirectReference().getObjNumber(),
                title.toUnicodeString()
            );

            alreadyVisitedOutlinesSet.add(current);
            addOutlineToPage(currentOutline, current, srcDoc);
            if (parentOutline.getChildren() == null) parentOutline.setChildren(new ArrayList<PdfOutlineTreeNodeDto>());
            parentOutline.getChildren().add(currentOutline);

            PdfDictionary first = current.getAsDictionary(PdfName.First);
            PdfDictionary next = current.getAsDictionary(PdfName.Next);

            if (first != null) {
                if (alreadyVisitedOutlinesSet.contains(first)) {
                    throw new PdfException(
                        MessageFormatUtil.format(KernelExceptionMessageConstant.CORRUPTED_OUTLINE_DICTIONARY_HAS_INFINITE_LOOP, first)
                    );
                }
                // Down in hierarchy; when returning up, process `next`.
                nextUnprocessedChildForParentMap.put(parentOutline, next);
                parentOutline = currentOutline;
                current = first;
            } else if (next != null) {
                if (alreadyVisitedOutlinesSet.contains(next)) {
                    throw new PdfException(
                        MessageFormatUtil.format(KernelExceptionMessageConstant.CORRUPTED_OUTLINE_DICTIONARY_HAS_INFINITE_LOOP, next)
                    );
                }
                // Next sibling in hierarchy
                current = next;
            } else {
                // Up in hierarchy using 'nextUnprocessedChildForParentMap'.
                current = null;
                while (current == null && parentOutline != null) {
                    parentOutline = parentOutline.getParent();
                    if (parentOutline != null) {
                        current = nextUnprocessedChildForParentMap.get(parentOutline);
                    }
                }
            }
        }

        return outlines;
    }

    public Optional<MediaDetailsDto> getMediaDetail(Long id) {
        log.debug("Request to get Media details : {}", id);
        Optional<MediaDetailsDto> mediaDetailsDto = mediaRepository.findOneWithEagerRelationships(id).map(mediaDetailsMapper::toDto);
        if (mediaDetailsDto.isPresent()) {
            String fileName = mediaDetailsDto.map(MediaDetailsDto::getFileName).orElse("");
            MediaDetailsDto dto = mediaDetailsDto.get();
            try {
                PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC + fileName));
                dto.setLastPageNumber(srcDoc.getNumberOfPages());
                try {
                    dto.setOutlines(getOutline(srcDoc));
                } catch (Exception e) {
                    srcDoc.close();
                    log.debug("Error: ", e);
                }
                srcDoc.close();
            } catch (Exception e) {
                log.debug("Error: ", e);
            }
        }
        return mediaDetailsDto;
    }

    public Optional<PageImageTransferDto> getPageImage(Long mediaId, int pageNumber) {
        PageImageTransferDto dto = new PageImageTransferDto();
        Optional<Media> media = mediaRepository.findOneWithEagerRelationships(mediaId);
        if (media.isPresent()) {
            String fileName = media.map(Media::getFileName).orElse("");
            try {
                PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC + fileName));
                PdfPage page = srcDoc.getPage(pageNumber);
                PdfResources resources = page.getResources();
                PdfDictionary dictionary = resources.getResource(PdfName.XObject);
                Set<PdfName> names = dictionary.keySet();
                Set<Map.Entry<PdfName, PdfObject>> namedObjects = dictionary.entrySet();
                namedObjects
                    .iterator()
                    .forEachRemaining(Entry -> {
                        PdfObject obj = Entry.getValue();

                        if (obj instanceof PdfStream stream) {
                            PdfImageXObject pImg = new PdfImageXObject(stream);
                            dto.setImage(pImg.getImageBytes());
                        }
                    });

                dto.setPageNumber(pageNumber);
                srcDoc.close();
            } catch (Exception e) {
                log.debug("Error: ", e);
            }
        }
        return Optional.of(dto);
    }
}
