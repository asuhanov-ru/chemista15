package com.chemista15.service;

import com.chemista15.domain.*; // for static metamodels
import com.chemista15.domain.Collection;
import com.chemista15.repository.CollectionRepository;
import com.chemista15.service.criteria.CollectionCriteria;
import com.chemista15.service.dto.CollectionDTO;
import com.chemista15.service.mapper.CollectionMapper;
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
 * Service for executing complex queries for {@link Collection} entities in the database.
 * The main input is a {@link CollectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CollectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollectionQueryService extends QueryService<Collection> {

    private final Logger log = LoggerFactory.getLogger(CollectionQueryService.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    public CollectionQueryService(CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
    }

    /**
     * Return a {@link Page} of {@link CollectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollectionDTO> findByCriteria(CollectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Collection> specification = createSpecification(criteria);
        return collectionRepository.findAll(specification, page).map(collectionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Collection> specification = createSpecification(criteria);
        return collectionRepository.count(specification);
    }

    /**
     * Function to convert {@link CollectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Collection> createSpecification(CollectionCriteria criteria) {
        Specification<Collection> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Collection_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Collection_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Collection_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Collection_.description));
            }
            if (criteria.getMediaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMediaId(), root -> root.join(Collection_.media, JoinType.LEFT).get(Media_.id))
                );
            }
        }
        return specification;
    }
}
