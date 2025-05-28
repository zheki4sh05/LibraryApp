package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.model.StorageModel;
import com.library.LibraryApp.core.repository.EditionRepository;
import com.library.LibraryApp.core.repository.StorageRepository;
import com.library.LibraryApp.core.service.StorageService;
import com.library.LibraryApp.application.dto.SearchStorageDto;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class LibraryStorageService implements StorageService {

    private final StorageRepository storageRepository;
    private final EditionRepository editionRepository;


    @Override
    public Mono<StorageModel> create(StorageModel storage) {

        return editionRepository.findById(storage.getEdition())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id" + storage.getEdition())))
                .flatMap(editionModel -> storageRepository.save(storage));
    }



    @Override
    public Mono<UUID> deleteById(UUID id) {
        return storageRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти расположение по id "+id)))
                .map(storageRepository::delete)
                .thenReturn(id);
    }

    @Override
    public Mono<StorageModel> update(StorageModel entity) {
        return storageRepository.save(entity);
    }

    @Override
    public Mono<Page<StorageModel>> fetch(SearchStorageDto searchStorageDto, Pageable pageable) {
        return storageRepository.fetchStorages(searchStorageDto, pageable);
//        if(searchStorageDto.dateTo().isBefore(searchStorageDto.dateFrom())){
//            throw new BadRequestException();
//        }
//        int size = pageable.getPageSize();
//        int offset = SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), size);
//        String sql = SqlQueryFactoryUtil.createStorageQuery();
//        Flux<StorageEntity> storageFlux = fetchQueries.fetchStorages(sql, searchStorageDto, size, offset);
//        return storageFlux.collectList()
//                .zipWith(storageRepository.count())
//                .map(tuple->
//                     new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())
//                );
    }

    @Override
    public Mono<Page<StorageModel>> findAllByEdition(UUID editionId, Pageable pageable) {
        return storageRepository.findAllByEditionId(editionId, pageable)
                .collectList()
                .zipWith(storageRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

}
