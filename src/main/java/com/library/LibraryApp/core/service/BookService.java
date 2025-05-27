package com.library.LibraryApp.core.service;

import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.core.model.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BookService {


    @Transactional
    Mono<BookModel> update(BookModel book);

    @Transactional
    Mono<UUID> delete(UUID id);

    @Transactional
    Mono<BookModel> save(BookModel newBook);


    @Transactional(readOnly = true)
    Mono<Page<BookModel>> fetch(SearchBookDto searchBookDto, Pageable pageable);
}
