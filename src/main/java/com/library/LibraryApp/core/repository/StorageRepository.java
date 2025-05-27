package com.library.LibraryApp.core.repository;

import com.library.LibraryApp.application.dto.SearchStorageDto;
import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.core.model.StorageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface StorageRepository  {

    Flux<StorageModel> findAllByEditionId(@Param("id") UUID uuid, Pageable pageable);

    Mono<StorageModel> save(StorageModel storage);

    Mono<StorageModel> findById(UUID id);

    Mono<Void> delete(StorageModel storageModel);

    Mono<Page<StorageModel>> fetchStorages(SearchStorageDto searchStorageDto, Pageable pageable);

    Mono<Long> count();
}
