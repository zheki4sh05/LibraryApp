package com.library.LibraryApp.web.mapper;

import com.library.LibraryApp.core.entity.Author;
import com.library.LibraryApp.web.dto.AuthorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mappings({
            @Mapping(target = "id", expression = "java(author.getId().toString())"),
            @Mapping(target = "name", source = "name")

    })
    AuthorDto toDto(Author author);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name")
    })
    Author toNewEntity(AuthorDto createAuthorDto);


    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.fromString(authorDto.getId()))"),
            @Mapping(target = "name", source = "name")
    })
    Author toEntity(AuthorDto authorDto);
}
