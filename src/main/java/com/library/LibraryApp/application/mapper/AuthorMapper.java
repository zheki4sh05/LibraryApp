package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.AuthorDto;
import com.library.LibraryApp.application.dto.CreateAuthorDto;
import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.core.model.AuthorModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(AuthorModel author);

    
    @Mapping(target = "id", ignore = true)
    AuthorModel toNewModel(CreateAuthorDto createAuthorDto);
    AuthorEntity fromModel(AuthorModel newAuthor);


    AuthorModel toModel(AuthorEntity saved);

    @Mapping(target = "id", source = "id")
    AuthorModel toModel(CreateAuthorDto authorDto, UUID id);
}
