package com.chemista15.service.mapper;

import com.chemista15.domain.Author;
import com.chemista15.domain.Book;
import com.chemista15.domain.Media;
import com.chemista15.service.dto.AuthorDTO;
import com.chemista15.service.dto.BookDTO;
import com.chemista15.service.dto.MediaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Book} and its DTO {@link BookDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookMapper extends EntityMapper<BookDTO, Book> {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorName")
    @Mapping(target = "media", source = "media", qualifiedByName = "mediaFileName")
    BookDTO toDto(Book s);

    @Named("authorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AuthorDTO toDtoAuthorName(Author author);

    @Named("mediaFileName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fileName", source = "fileName")
    MediaDTO toDtoMediaFileName(Media media);
}
