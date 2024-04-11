package com.chemista15.service.mapper;

import com.chemista15.domain.Collection;
import com.chemista15.service.dto.CollectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Collection} and its DTO {@link CollectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CollectionMapper extends EntityMapper<CollectionDTO, Collection> {}
