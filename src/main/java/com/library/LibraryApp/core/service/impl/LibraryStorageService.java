package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.entity.Storage;
import com.library.LibraryApp.core.repository.EditionRepository;
import com.library.LibraryApp.core.repository.FetchQueries;
import com.library.LibraryApp.core.repository.StorageRepository;
import com.library.LibraryApp.core.service.StorageService;
import com.library.LibraryApp.core.util.SqlQueryFactoryUtil;
import com.library.LibraryApp.web.dto.SearchStorageDto;
import com.library.LibraryApp.web.exceptions.BadRequestException;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class LibraryStorageService implements StorageService {

    private final StorageRepository storageRepository;
    private final EditionRepository editionRepository;
    private final FetchQueries fetchQueries;

    @Override
    public Mono<Storage> create(Storage storage) {

        return editionRepository.findById(storage.getEdition())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id" + storage.getEdition())))
                .flatMap(book -> storageRepository.save(storage));
    }



    @Override
    public Mono<String> deleteById(String id) {
        return storageRepository.findById(UUID.fromString(id))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти расположение по id "+id)))
                .map(storageRepository::delete)
                .thenReturn(id);
    }

    @Override
    public Mono<Storage> update(Storage entity) {
        return storageRepository.save(entity);
    }

    @Override
    public Mono<Page<Storage>> fetch(SearchStorageDto searchStorageDto, Pageable pageable) {
        if(searchStorageDto.dateTo().isBefore(searchStorageDto.dateFrom())){
            throw new BadRequestException();
        }
        int size = pageable.getPageSize();
        int offset = SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), size);
        String sql = SqlQueryFactoryUtil.createStorageQuery();
        Flux<Storage> storageFlux = fetchQueries.fetchStorages(sql, searchStorageDto, size, offset);
        return storageFlux.collectList()
                .zipWith(storageRepository.count())
                .map(tuple->
                     new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())
                );
    }

    @Override
    public Mono<Page<Storage>> findAllByEdition(String editionId, Pageable pageable) {
        return storageRepository.findAllByEditionId(UUID.fromString(editionId), pageable)
                .collectList()
                .zipWith(storageRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

}
