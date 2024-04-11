package com.chemista15.service.mapper;

import com.chemista15.domain.Collection;
import com.chemista15.domain.Media;
import com.chemista15.service.dto.CollectionDTO;
import com.chemista15.service.dto.MediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {
    @Mapping(target = "collection", source = "collection", qualifiedByName = "collectionName")
    MediaDTO toDto(Media s);

    @Named("collectionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CollectionDTO toDtoCollectionName(Collection collection);
}
