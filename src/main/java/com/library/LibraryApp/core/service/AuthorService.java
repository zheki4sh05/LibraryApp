package com.library.LibraryApp.core.service;

import com.library.LibraryApp.application.dto.SearchAuthorDto;
import com.library.LibraryApp.core.model.AuthorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthorService {

    @Transactional
    Mono<AuthorModel> create(AuthorModel createAuthorDto);

    @Transactional(readOnly = true)
    Mono<Page<AuthorModel>> fetch(SearchAuthorDto searchAuthorDto, Pageable pageable);

    @Transactional(readOnly = true)
    Mono<AuthorModel> findById(UUID id);

    @Transactional
    Mono<AuthorModel> update(AuthorModel authorDto);

    @Transactional
    Mono<UUID> deleteById(UUID id);
}
