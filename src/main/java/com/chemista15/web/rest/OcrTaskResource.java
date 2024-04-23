package com.chemista15.web.rest;

import com.chemista15.repository.OcrTaskRepository;
import com.chemista15.service.OcrTaskQueryService;
import com.chemista15.service.OcrTaskService;
import com.chemista15.service.criteria.OcrTaskCriteria;
import com.chemista15.service.dto.OcrTaskDTO;
import com.chemista15.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.chemista15.domain.OcrTask}.
 */
@RestController
@RequestMapping("/api/ocr-tasks")
public class OcrTaskResource {

    private final Logger log = LoggerFactory.getLogger(OcrTaskResource.class);

    private static final String ENTITY_NAME = "ocrTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OcrTaskService ocrTaskService;

    private final OcrTaskRepository ocrTaskRepository;

    private final OcrTaskQueryService ocrTaskQueryService;

    public OcrTaskResource(OcrTaskService ocrTaskService, OcrTaskRepository ocrTaskRepository, OcrTaskQueryService ocrTaskQueryService) {
        this.ocrTaskService = ocrTaskService;
        this.ocrTaskRepository = ocrTaskRepository;
        this.ocrTaskQueryService = ocrTaskQueryService;
    }

    /**
     * {@code POST  /ocr-tasks} : Create a new ocrTask.
     *
     * @param ocrTaskDTO the ocrTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocrTaskDTO, or with status {@code 400 (Bad Request)} if the ocrTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OcrTaskDTO> createOcrTask(@RequestBody OcrTaskDTO ocrTaskDTO) throws URISyntaxException {
        log.debug("REST request to save OcrTask : {}", ocrTaskDTO);
        if (ocrTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new ocrTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ocrTaskDTO = ocrTaskService.save(ocrTaskDTO);
        return ResponseEntity.created(new URI("/api/ocr-tasks/" + ocrTaskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ocrTaskDTO.getId().toString()))
            .body(ocrTaskDTO);
    }

    /**
     * {@code PUT  /ocr-tasks/:id} : Updates an existing ocrTask.
     *
     * @param id the id of the ocrTaskDTO to save.
     * @param ocrTaskDTO the ocrTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrTaskDTO,
     * or with status {@code 400 (Bad Request)} if the ocrTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocrTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcrTaskDTO> updateOcrTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OcrTaskDTO ocrTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OcrTask : {}, {}", id, ocrTaskDTO);
        if (ocrTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ocrTaskDTO = ocrTaskService.update(ocrTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrTaskDTO.getId().toString()))
            .body(ocrTaskDTO);
    }

    /**
     * {@code PATCH  /ocr-tasks/:id} : Partial updates given fields of an existing ocrTask, field will ignore if it is null
     *
     * @param id the id of the ocrTaskDTO to save.
     * @param ocrTaskDTO the ocrTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrTaskDTO,
     * or with status {@code 400 (Bad Request)} if the ocrTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ocrTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ocrTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OcrTaskDTO> partialUpdateOcrTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OcrTaskDTO ocrTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OcrTask partially : {}, {}", id, ocrTaskDTO);
        if (ocrTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OcrTaskDTO> result = ocrTaskService.partialUpdate(ocrTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ocr-tasks} : get all the ocrTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocrTasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OcrTaskDTO>> getAllOcrTasks(
        OcrTaskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get OcrTasks by criteria: {}", criteria);

        Page<OcrTaskDTO> page = ocrTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ocr-tasks/count} : count all the ocrTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOcrTasks(OcrTaskCriteria criteria) {
        log.debug("REST request to count OcrTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(ocrTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ocr-tasks/:id} : get the "id" ocrTask.
     *
     * @param id the id of the ocrTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocrTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcrTaskDTO> getOcrTask(@PathVariable("id") Long id) {
        log.debug("REST request to get OcrTask : {}", id);
        Optional<OcrTaskDTO> ocrTaskDTO = ocrTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ocrTaskDTO);
    }

    /**
     * {@code DELETE  /ocr-tasks/:id} : delete the "id" ocrTask.
     *
     * @param id the id of the ocrTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcrTask(@PathVariable("id") Long id) {
        log.debug("REST request to delete OcrTask : {}", id);
        ocrTaskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
