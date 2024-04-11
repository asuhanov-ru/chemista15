package com.chemista15.service;

import com.chemista15.domain.*; // for static metamodels
import com.chemista15.domain.Media;
import com.chemista15.repository.MediaRepository;
import com.chemista15.service.criteria.MediaCriteria;
import com.chemista15.service.dto.MediaDTO;
import com.chemista15.service.mapper.MediaMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Media} entities in the database.
 * The main input is a {@link MediaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MediaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MediaQueryService extends QueryService<Media> {

    private final Logger log = LoggerFactory.getLogger(MediaQueryService.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    public MediaQueryService(MediaRepository mediaRepository, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    /**
     * Return a {@link Page} of {@link MediaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MediaDTO> findByCriteria(MediaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaRepository.findAll(specification, page).map(mediaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MediaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Media> specification = createSpecification(criteria);
        return mediaRepository.count(specification);
    }

    /**
     * Function to convert {@link MediaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Media> createSpecification(MediaCriteria criteria) {
        Specification<Media> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Media_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Media_.uuid));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), Media_.fileName));
            }
            if (criteria.getFileType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileType(), Media_.fileType));
            }
            if (criteria.getFileDesc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileDesc(), Media_.fileDesc));
            }
            if (criteria.getCollectionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCollectionId(), root -> root.join(Media_.collection, JoinType.LEFT).get(Collection_.id))
                );
            }
        }
        return specification;
    }
}
