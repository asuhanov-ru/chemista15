package com.chemista15.code.media.controller;

import com.chemista15.code.media.dto.MediaDetailsDto;
import com.chemista15.code.media.dto.PageImageTransferDto;
import com.chemista15.code.pdf.sevice.PdfDocService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class MediaResourceV2 {

    private final Logger log = LoggerFactory.getLogger(MediaResourceV2.class);

    private final PdfDocService pdfDocService;

    public MediaResourceV2(PdfDocService pdfDocService) {
        this.pdfDocService = pdfDocService;
    }

    /**
     * {@code GET  /v2/media/:id} : get the "id" media.
     *
     * @param id the id of the media to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the media, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v2/media/{id}")
    public ResponseEntity<MediaDetailsDto> getMedia(@PathVariable Long id) {
        log.debug("REST request to get Media : {}", id);

        return ResponseUtil.wrapOrNotFound(pdfDocService.getMediaDetail(id));
    }

    /**
     * {@code GET  /V2/page-images/:id} : get the "id" pageImage.
     *
     * @param id the id of the pageImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pageImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v2/page-images/{id}")
    public ResponseEntity<PageImageTransferDto> getPageImage(@PathVariable Long id, @RequestParam int pageNumber) throws Exception {
        return ResponseUtil.wrapOrNotFound(pdfDocService.getPageImage(id, pageNumber));
    }
}
