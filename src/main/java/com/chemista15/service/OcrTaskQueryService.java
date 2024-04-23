package com.chemista15.service;

import com.chemista15.domain.*; // for static metamodels
import com.chemista15.domain.OcrTask;
import com.chemista15.repository.OcrTaskRepository;
import com.chemista15.service.criteria.OcrTaskCriteria;
import com.chemista15.service.dto.OcrTaskDTO;
import com.chemista15.service.mapper.OcrTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OcrTask} entities in the database.
 * The main input is a {@link OcrTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OcrTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OcrTaskQueryService extends QueryService<OcrTask> {

    private final Logger log = LoggerFactory.getLogger(OcrTaskQueryService.class);

    private final OcrTaskRepository ocrTaskRepository;

    private final OcrTaskMapper ocrTaskMapper;

    public OcrTaskQueryService(OcrTaskRepository ocrTaskRepository, OcrTaskMapper ocrTaskMapper) {
        this.ocrTaskRepository = ocrTaskRepository;
        this.ocrTaskMapper = ocrTaskMapper;
    }

    /**
     * Return a {@link Page} of {@link OcrTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OcrTaskDTO> findByCriteria(OcrTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OcrTask> specification = createSpecification(criteria);
        return ocrTaskRepository.findAll(specification, page).map(ocrTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OcrTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OcrTask> specification = createSpecification(criteria);
        return ocrTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link OcrTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OcrTask> createSpecification(OcrTaskCriteria criteria) {
        Specification<OcrTask> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OcrTask_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), OcrTask_.uuid));
            }
            if (criteria.getMediaId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMediaId(), OcrTask_.mediaId));
            }
            if (criteria.getPageNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageNumber(), OcrTask_.pageNumber));
            }
            if (criteria.getJobStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobStatus(), OcrTask_.jobStatus));
            }
            if (criteria.getCreateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTime(), OcrTask_.createTime));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), OcrTask_.startTime));
            }
            if (criteria.getStopTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStopTime(), OcrTask_.stopTime));
            }
        }
        return specification;
    }
}
