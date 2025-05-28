package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.CreateEditionDto;
import com.library.LibraryApp.application.dto.EditionDto;
import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.core.model.EditionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface EditionMapper {

    EditionDto toDto(EditionModel entity);

    @Mapping(target = "id", ignore = true)
    EditionModel toNewModel(CreateEditionDto editionDto);

    @Mapping(target = "id", source = "id")
    EditionModel toModel(CreateEditionDto editionDto, UUID id);

    EditionModel toModel(EditionEntity edition);

    EditionEntity toEntity(EditionModel edition);
}
