package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.CreateStorageDto;
import com.library.LibraryApp.application.dto.StorageDto;
import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.core.model.StorageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StorageMapper {



    StorageDto toDto(StorageModel entity);

    @Mapping(target = "id", ignore = true)
    StorageModel toNewModel(CreateStorageDto storageDto);

    @Mapping(target = "id", source = "id")
    StorageModel toModel(CreateStorageDto storageEntity, UUID id);

    StorageModel toModel(StorageEntity storageEntity);

    StorageEntity toEntity(StorageModel storage);
}
