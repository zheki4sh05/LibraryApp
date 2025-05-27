package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.AuthorDto;
import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.core.model.AuthorModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    
    @Mapping(target = "id", expression = "java(author.getId().toString())")
    AuthorDto toDto(AuthorModel author);

    
    @Mapping(target = "id", ignore = true)
    AuthorModel toNewModel(AuthorDto createAuthorDto);
    AuthorEntity fromModel(AuthorModel newAuthor);


    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.fromString(authorDto.getId()))"),
            @Mapping(target = "name", source = "name")
    })
    AuthorEntity toEntity(AuthorDto authorDto);

    AuthorModel toModel(AuthorEntity saved);


    AuthorModel toModel(AuthorDto authorDto);
}
