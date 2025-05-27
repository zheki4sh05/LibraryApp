package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.EditionDto;
import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.core.model.EditionModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EditionMapper {


    @Mappings({
            @Mapping(target = "id",expression = "java(java.util.UUID.fromString(dto.id()))"),
            @Mapping(target = "bookId", expression = "java(java.util.UUID.fromString(dto.book()))"),
            @Mapping(target = "pages", source = "pages"),
            @Mapping(target = "publication", source = "publication"),
            @Mapping(target = "number", source = "number", qualifiedByName = "mapIntegerToShort"),
            @Mapping(target = "isbn", source = "isbn")
    })
    EditionEntity toEntity(EditionDto dto);


    @Mapping(target = "book", expression = "java(entity.getBookId().toString())")
    @Mapping(target = "id",  expression = "java(entity.getId().toString())")
    EditionDto toDto(EditionModel entity);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bookId", expression = "java(java.util.UUID.fromString(editionDto.book()))")
    EditionModel toNewModel(EditionDto editionDto);


    EditionModel toModel(EditionDto editionDto);

    EditionModel toModel(EditionEntity edition);

    EditionEntity toEntity(EditionModel edition);
}
