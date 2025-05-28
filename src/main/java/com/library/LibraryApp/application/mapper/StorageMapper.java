package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.StorageDto;
import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.core.model.StorageModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface StorageMapper {



    StorageDto toDto(StorageModel entity);

    @Mapping(target = "id", ignore = true)
    StorageModel toNewModel(StorageDto storageDto);

    StorageModel toModel(StorageDto storageEntity);
    StorageModel toModel(StorageEntity storageEntity);

    StorageEntity toEntity(StorageModel storage);
}
