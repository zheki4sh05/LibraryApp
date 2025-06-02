package com.library.LibraryApp.core.repository;


import com.library.LibraryApp.application.dto.SearchAuthorDto;
import com.library.LibraryApp.core.model.AuthorModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface AuthorRepository {

    Mono<AuthorModel> save(AuthorModel newAuthor);


    Mono<AuthorModel> findById(UUID id);

    Mono<Page<AuthorModel>> fetchAuthors(SearchAuthorDto searchAuthorDto, Pageable pageable);

    Mono<Void> delete(AuthorModel author);
}
