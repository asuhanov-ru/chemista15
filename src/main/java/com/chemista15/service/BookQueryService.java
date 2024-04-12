package com.chemista15.service;

import com.chemista15.domain.*; // for static metamodels
import com.chemista15.domain.Book;
import com.chemista15.repository.BookRepository;
import com.chemista15.service.criteria.BookCriteria;
import com.chemista15.service.dto.BookDTO;
import com.chemista15.service.mapper.BookMapper;
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
 * Service for executing complex queries for {@link Book} entities in the database.
 * The main input is a {@link BookCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BookDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookQueryService extends QueryService<Book> {

    private final Logger log = LoggerFactory.getLogger(BookQueryService.class);

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookQueryService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Return a {@link Page} of {@link BookDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookDTO> findByCriteria(BookCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Book> specification = createSpecification(criteria);
        return bookRepository.findAll(specification, page).map(bookMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Book> specification = createSpecification(criteria);
        return bookRepository.count(specification);
    }

    /**
     * Function to convert {@link BookCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Book> createSpecification(BookCriteria criteria) {
        Specification<Book> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Book_.id));
            }
            if (criteria.getUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUuid(), Book_.uuid));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Book_.name));
            }
            if (criteria.getMediaStartPage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMediaStartPage(), Book_.mediaStartPage));
            }
            if (criteria.getMediaEndPage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMediaEndPage(), Book_.mediaEndPage));
            }
            if (criteria.getAuthorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAuthorId(), root -> root.join(Book_.author, JoinType.LEFT).get(Author_.id))
                );
            }
            if (criteria.getMediaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMediaId(), root -> root.join(Book_.media, JoinType.LEFT).get(Media_.id))
                );
            }
        }
        return specification;
    }
}
