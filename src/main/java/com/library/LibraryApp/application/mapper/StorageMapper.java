package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.StorageDto;
import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.core.model.StorageModel;
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
    StorageEntity toEntity(StorageDto dto);


    StorageDto toDto(StorageModel entity);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "rack", source = "rack"),
            @Mapping(target = "accounting", source = "accounting"),
            @Mapping(target = "taken", source = "taken"),
            @Mapping(target = "edition", expression = "java(java.util.UUID.fromString(storageDto.edition()))")
    })
    StorageEntity toNewEntity(StorageDto storageDto);

    @Mapping(target = "id", ignore = true)
    StorageModel toNewModel(StorageDto storageDto);

    StorageModel toModel(StorageDto storageEntity);

    StorageEntity toEntity(StorageModel storage);
}
