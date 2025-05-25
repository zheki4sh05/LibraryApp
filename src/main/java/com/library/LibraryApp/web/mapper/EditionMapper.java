package com.library.LibraryApp.web.mapper;

import com.library.LibraryApp.core.entity.Edition;
import com.library.LibraryApp.web.dto.EditionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.UUID;

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
    Edition toEntity(EditionDto dto);

    @Mappings({
            @Mapping(target = "book", expression = "java(entity.getBookId().toString())"),
            @Mapping(target = "pages", source = "pages"),
            @Mapping(target = "publication", source = "publication"),
            @Mapping(target = "number", source = "number", qualifiedByName = "mapShortToInteger"),
            @Mapping(target = "isbn", source = "isbn"),
            @Mapping(target = "id",  expression = "java(entity.getId().toString())")
    })
    EditionDto toDto(Edition entity);


    @Named("mapIntegerToShort")
    default Short mapIntegerToShort(Integer value) {
        short r;
        return value != null ? value.shortValue() : null;
    }

    @Named("mapShortToInteger")
    default Integer mapShortToInteger(Short value) {
        return value != null ? Integer.valueOf(value) : null;
    }

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "bookId", expression = "java(java.util.UUID.fromString(editionDto.book()))"),
            @Mapping(target = "pages", source = "pages"),
            @Mapping(target = "publication", source = "publication"),
            @Mapping(target = "number", source = "number", qualifiedByName = "mapIntegerToShort"),
            @Mapping(target = "isbn", source = "isbn")
    })
    Edition toNewEntity(EditionDto editionDto);
}
