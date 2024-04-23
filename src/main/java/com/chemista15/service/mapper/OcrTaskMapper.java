package com.chemista15.service.mapper;

import com.chemista15.domain.OcrTask;
import com.chemista15.service.dto.OcrTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OcrTask} and its DTO {@link OcrTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface OcrTaskMapper extends EntityMapper<OcrTaskDTO, OcrTask> {}
