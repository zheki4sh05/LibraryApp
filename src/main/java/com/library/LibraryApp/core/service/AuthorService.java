package com.library.LibraryApp.core.service;

import com.library.LibraryApp.core.entity.Author;
import com.library.LibraryApp.web.dto.SearchAuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface AuthorService {

    @Transactional
    Mono<Author> create(Author createAuthorDto);

    @Transactional(readOnly = true)
    Mono<Page<Author>> fetch(SearchAuthorDto searchAuthorDto, Pageable pageable);

    @Transactional(readOnly = true)
    Mono<Author> findById(String id);

    @Transactional
    Mono<Author> update(Author authorDto);

    @Transactional
    Mono<String> deleteById(String id);
}
