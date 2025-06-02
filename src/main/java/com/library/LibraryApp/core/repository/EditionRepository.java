package com.library.LibraryApp.core.repository;

import com.library.LibraryApp.application.dto.SearchEditionDto;
import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.core.model.EditionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface EditionRepository{
    Mono<EditionModel> save(EditionModel edition);

    Mono<EditionModel> findById(UUID id);

    Mono<Void> delete(EditionModel editionModel);

    Mono<Long> count();

    Mono<Page<EditionModel>> fetchEditions(SearchEditionDto searchEditionDto, Pageable pageable);

    Mono<EditionModel> findByIsbn(String isbn);
}
