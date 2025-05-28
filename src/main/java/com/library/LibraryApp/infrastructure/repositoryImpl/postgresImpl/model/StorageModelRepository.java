package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.SearchStorageDto;
import com.library.LibraryApp.application.mapper.StorageMapper;
import com.library.LibraryApp.core.model.StorageModel;
import com.library.LibraryApp.core.repository.StorageRepository;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.StorageR2dbcRepository;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util.FetchQueries;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Component
@Slf4j
public class StorageModelRepository implements StorageRepository {

    private final StorageR2dbcRepository storageR2dbcRepository;
    private StorageMapper storageMapper;
    private final FetchQueries fetchQueries;


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
        log.info(searchStorageDto.toString());
        return fetchQueries.fetchStorages(searchStorageDto, pageable)
                .map(storageMapper::toModel)
                .collectList()
                .flatMap(storageModels -> storageR2dbcRepository.count()
                        .map(total->new PageImpl<>(storageModels, pageable, total))
                );
    }

    @Override
    public Mono<Long> count() {
        return storageR2dbcRepository.count();
    }
}
