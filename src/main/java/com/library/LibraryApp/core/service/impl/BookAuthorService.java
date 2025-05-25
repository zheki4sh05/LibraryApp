package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.entity.Author;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.core.repository.FetchQueries;
import com.library.LibraryApp.core.service.AuthorService;
import com.library.LibraryApp.core.util.SqlQueryFactoryUtil;
import com.library.LibraryApp.web.dto.SearchAuthorDto;
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
public class BookAuthorService implements AuthorService {

    private final AuthorRepository authorRepository;
    private final FetchQueries fetchQueries;


    @Override
    public Mono<Author> create(Author newAuthor) {
        return authorRepository.save(newAuthor);
    }

    @Override
    public Mono<Page<Author>> fetch(SearchAuthorDto searchAuthorDto, Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), size);
        String sql = SqlQueryFactoryUtil.createAuthorQuery();
        Flux<Author> authors = fetchQueries.fetchAuthors(sql, searchAuthorDto, size, offset);
        return authors.collectList()
                .zipWith(authorRepository.count())
                .map(tuple->
                        new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())
                );
    }


    @Override
    public Mono<Author> findById(String id) {

        return authorRepository.findById(UUID.fromString(id))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id " +id)));
    }

    @Override
    public Mono<Author> update(Author updatedAuthor) {
        return authorRepository.save(updatedAuthor);
    }

    @Override
    public Mono<String> deleteById(String id) {
        return authorRepository.findById(UUID.fromString(id))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти для удаления автора по id "+id)))
                .flatMap(authorRepository::delete)
                .thenReturn(id);
    }
}
