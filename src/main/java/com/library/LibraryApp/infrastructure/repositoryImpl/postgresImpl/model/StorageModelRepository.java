package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.application.mapper.*;
import com.library.LibraryApp.core.model.*;
import com.library.LibraryApp.core.repository.*;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;

import java.util.*;

@AllArgsConstructor
@Component
@Slf4j
public class StorageModelRepository implements StorageRepository {

    private final StorageR2dbcRepository storageR2dbcRepository;
    private StorageMapper storageMapper;


    @Override
    public Flux<StorageModel> findAllByEditionId(UUID uuid, Pageable pageable) {
        return storageR2dbcRepository.findAllByEditionId(uuid,pageable ).map(storageMapper::toModel);
    }

    @Override
    public Mono<StorageModel> save(StorageModel storage) {
        return storageR2dbcRepository.save(storageMapper.toEntity(storage)).map(storageMapper::toModel);
    }

    @Override
    public Mono<StorageModel> findById(UUID id) {
        return storageR2dbcRepository.findById(id).map(storageMapper::toModel);
    }

    @Override
    public Mono<Void> delete(StorageModel storageModel) {
        return storageR2dbcRepository.delete(storageMapper.toEntity(storageModel));
    }

    @Override
    public Mono<Page<StorageModel>> fetchStorages(SearchStorageDto searchStorageDto, Pageable pageable) {
        return storageR2dbcRepository.findByStatusAndAccountingBetweenAndRackBetweenAndEdition(
                searchStorageDto.getStatus().name(),
                        searchStorageDto.getDateFrom(),
                        searchStorageDto.getDateTo(),
                        searchStorageDto.getRackMin(),
                        searchStorageDto.getRackMax(),
                        searchStorageDto.getEdition(),
                        pageable)
                .map(storageMapper::toModel)
                .collectList()
                .flatMap(storageModels -> storageR2dbcRepository.count()
                        .map(total->
                                new PageImpl<>(storageModels, pageable, total)
                        )
                );

    }

    @Override
    public Mono<Long> count() {
        return storageR2dbcRepository.count();
    }
}
