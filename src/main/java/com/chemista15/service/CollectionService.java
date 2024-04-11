package com.chemista15.service;

import com.chemista15.service.dto.CollectionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.chemista15.domain.Collection}.
 */
public interface CollectionService {
    /**
     * Save a collection.
     *
     * @param collectionDTO the entity to save.
     * @return the persisted entity.
     */
    CollectionDTO save(CollectionDTO collectionDTO);

    /**
     * Updates a collection.
     *
     * @param collectionDTO the entity to update.
     * @return the persisted entity.
     */
    CollectionDTO update(CollectionDTO collectionDTO);

    /**
     * Partially updates a collection.
     *
     * @param collectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CollectionDTO> partialUpdate(CollectionDTO collectionDTO);

    /**
     * Get the "id" collection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CollectionDTO> findOne(Long id);

    /**
     * Delete the "id" collection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
