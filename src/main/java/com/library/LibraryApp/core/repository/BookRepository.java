package com.library.LibraryApp.core.repository;


import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.core.model.AuthorModel;
import com.library.LibraryApp.core.model.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface BookRepository  {

    Mono<BookModel> save(BookModel newBook);
    Mono<Page<BookModel>> fetchBooks(SearchBookDto searchBookDto, Pageable pageable);
    Mono<Void> delete(BookModel book);

    Mono<BookModel> findById(UUID id);
}

