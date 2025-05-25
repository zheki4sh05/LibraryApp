package com.library.LibraryApp.core.service;

import com.library.LibraryApp.core.entity.Book;
import com.library.LibraryApp.web.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BookService {


    @Transactional
    Mono<Book> update(Book book);

    @Transactional
    Mono<String> delete(String id);

    @Transactional
    Mono<Book> save(Book newBook);


    @Transactional(readOnly = true)
    Mono<Page<Book>> fetch(SearchBookDto searchBookDto, Pageable pageable);
}
