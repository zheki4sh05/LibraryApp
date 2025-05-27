package com.library.LibraryApp.core.service;

import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.application.dto.SearchEditionDto;
import com.library.LibraryApp.core.model.EditionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EditionService {
    @Transactional
    Mono<EditionModel> create(EditionModel edition);

    @Transactional(readOnly = true)
    Mono<EditionModel> findById(UUID id);

    @Transactional
    Mono<EditionModel> update(EditionModel entity);

    @Transactional
    Mono<UUID> delete(UUID id);

    @Transactional(readOnly = true)
    Mono<Page<EditionModel>> fetch(SearchEditionDto searchEditionDto, Pageable pageable);
}
