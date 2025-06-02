package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.application.entity.*;
import com.library.LibraryApp.core.model.*;
import org.mapstruct.*;

import java.util.*;

@Mapper(componentModel = "spring")
public interface StorageMapper {



    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    StorageDto toDto(StorageModel entity);

    @Mapping(target = "id", ignore = true)
    StorageModel toNewModel(CreateStorageDto storageDto);

    @Mapping(target = "id", source = "id")
    StorageModel toModel(CreateStorageDto storageEntity, UUID id);

    StorageModel toModel(StorageEntity storageEntity);

    StorageEntity toEntity(StorageModel storage);
}
