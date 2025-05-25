package com.library.LibraryApp.core.service;

import com.library.LibraryApp.core.entity.Storage;
import com.library.LibraryApp.web.dto.SearchStorageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface StorageService {

    @Transactional
    Mono<Storage> create(Storage storage);

    Mono<String> deleteById(String id);

    Mono<Storage> update(Storage entity);

    Mono<Page<Storage>> fetch(SearchStorageDto searchStorageDto, Pageable pageable);

    Mono<Page<Storage>> findAllByEdition(String editionId, Pageable pageable);
}
