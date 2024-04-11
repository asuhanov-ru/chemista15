package com.chemista15.service.impl;

import com.chemista15.domain.Collection;
import com.chemista15.repository.CollectionRepository;
import com.chemista15.service.CollectionService;
import com.chemista15.service.dto.CollectionDTO;
import com.chemista15.service.mapper.CollectionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chemista15.domain.Collection}.
 */
@Service
@Transactional
public class CollectionServiceImpl implements CollectionService {

    private final Logger log = LoggerFactory.getLogger(CollectionServiceImpl.class);

    private final CollectionRepository collectionRepository;

    private final CollectionMapper collectionMapper;

    public CollectionServiceImpl(CollectionRepository collectionRepository, CollectionMapper collectionMapper) {
        this.collectionRepository = collectionRepository;
        this.collectionMapper = collectionMapper;
    }

    @Override
    public CollectionDTO save(CollectionDTO collectionDTO) {
        log.debug("Request to save Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    @Override
    public CollectionDTO update(CollectionDTO collectionDTO) {
        log.debug("Request to update Collection : {}", collectionDTO);
        Collection collection = collectionMapper.toEntity(collectionDTO);
        collection = collectionRepository.save(collection);
        return collectionMapper.toDto(collection);
    }

    @Override
    public Optional<CollectionDTO> partialUpdate(CollectionDTO collectionDTO) {
        log.debug("Request to partially update Collection : {}", collectionDTO);

        return collectionRepository
            .findById(collectionDTO.getId())
            .map(existingCollection -> {
                collectionMapper.partialUpdate(existingCollection, collectionDTO);

                return existingCollection;
            })
            .map(collectionRepository::save)
            .map(collectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CollectionDTO> findOne(Long id) {
        log.debug("Request to get Collection : {}", id);
        return collectionRepository.findById(id).map(collectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Collection : {}", id);
        collectionRepository.deleteById(id);
    }
}
