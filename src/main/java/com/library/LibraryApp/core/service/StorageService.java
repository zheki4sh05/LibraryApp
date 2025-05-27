package com.library.LibraryApp.core.service;

import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.application.dto.SearchStorageDto;
import com.library.LibraryApp.core.model.StorageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StorageService {

    @Transactional
    Mono<StorageModel> create(StorageModel storage);

    @Transactional
    Mono<UUID> deleteById(UUID id);

    @Transactional
    Mono<StorageModel> update(StorageModel entity);

    @Transactional(readOnly = true)
    Mono<Page<StorageModel>> fetch(SearchStorageDto searchStorageDto, Pageable pageable);

    @Transactional(readOnly = true)
    Mono<Page<StorageModel>> findAllByEdition(UUID editionId, Pageable pageable);
}
