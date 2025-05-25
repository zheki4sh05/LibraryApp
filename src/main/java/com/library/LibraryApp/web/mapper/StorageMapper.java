package com.library.LibraryApp.web.mapper;

import com.library.LibraryApp.core.entity.Storage;
import com.library.LibraryApp.web.dto.StorageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.fromString(dto.id()))"),
            @Mapping(target = "rack", source = "rack"),
            @Mapping(target = "accounting", source = "accounting"),
            @Mapping(target = "taken", source = "taken"),
            @Mapping(target = "edition", expression = "java(java.util.UUID.fromString(dto.edition()))")
    })
    Storage toEntity(StorageDto dto);

    @Mappings({
            @Mapping(target = "id", expression = "java(entity.getId().toString())"),
            @Mapping(target = "rack", source = "rack"),
            @Mapping(target = "accounting", source = "accounting"),
            @Mapping(target = "taken", source = "taken"),
            @Mapping(target = "edition", expression = "java(entity.getEdition().toString())")
    })
    StorageDto toDto(Storage entity);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "rack", source = "rack"),
            @Mapping(target = "accounting", source = "accounting"),
            @Mapping(target = "taken", source = "taken"),
            @Mapping(target = "edition", expression = "java(java.util.UUID.fromString(storageDto.edition()))")
    })
    Storage toNewEntity(StorageDto storageDto);
}
