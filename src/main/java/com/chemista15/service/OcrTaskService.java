package com.chemista15.service;

import com.chemista15.domain.OcrTask;
import com.chemista15.repository.OcrTaskRepository;
import com.chemista15.service.dto.OcrTaskDTO;
import com.chemista15.service.mapper.OcrTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chemista15.domain.OcrTask}.
 */
@Service
@Transactional
public class OcrTaskService {

    private final Logger log = LoggerFactory.getLogger(OcrTaskService.class);

    private final OcrTaskRepository ocrTaskRepository;

    private final OcrTaskMapper ocrTaskMapper;

    public OcrTaskService(OcrTaskRepository ocrTaskRepository, OcrTaskMapper ocrTaskMapper) {
        this.ocrTaskRepository = ocrTaskRepository;
        this.ocrTaskMapper = ocrTaskMapper;
    }

    /**
     * Save a ocrTask.
     *
     * @param ocrTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrTaskDTO save(OcrTaskDTO ocrTaskDTO) {
        log.debug("Request to save OcrTask : {}", ocrTaskDTO);
        OcrTask ocrTask = ocrTaskMapper.toEntity(ocrTaskDTO);
        ocrTask = ocrTaskRepository.save(ocrTask);
        return ocrTaskMapper.toDto(ocrTask);
    }

    /**
     * Update a ocrTask.
     *
     * @param ocrTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public OcrTaskDTO update(OcrTaskDTO ocrTaskDTO) {
        log.debug("Request to update OcrTask : {}", ocrTaskDTO);
        OcrTask ocrTask = ocrTaskMapper.toEntity(ocrTaskDTO);
        ocrTask = ocrTaskRepository.save(ocrTask);
        return ocrTaskMapper.toDto(ocrTask);
    }

    /**
     * Partially update a ocrTask.
     *
     * @param ocrTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OcrTaskDTO> partialUpdate(OcrTaskDTO ocrTaskDTO) {
        log.debug("Request to partially update OcrTask : {}", ocrTaskDTO);

        return ocrTaskRepository
            .findById(ocrTaskDTO.getId())
            .map(existingOcrTask -> {
                ocrTaskMapper.partialUpdate(existingOcrTask, ocrTaskDTO);

                return existingOcrTask;
            })
            .map(ocrTaskRepository::save)
            .map(ocrTaskMapper::toDto);
    }

    /**
     * Get one ocrTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OcrTaskDTO> findOne(Long id) {
        log.debug("Request to get OcrTask : {}", id);
        return ocrTaskRepository.findById(id).map(ocrTaskMapper::toDto);
    }

    /**
     * Delete the ocrTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OcrTask : {}", id);
        ocrTaskRepository.deleteById(id);
    }
}
