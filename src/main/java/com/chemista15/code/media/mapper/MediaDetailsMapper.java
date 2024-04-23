package com.chemista15.code.media.mapper;

import com.chemista15.code.media.dto.MediaDetailsDto;
import com.chemista15.domain.Media;
import com.chemista15.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaDetailsMapper extends EntityMapper<MediaDetailsDto, Media> {}
