package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.application.entity.*;
import com.library.LibraryApp.application.mapper.BookMapper;
import com.library.LibraryApp.core.model.BookModel;
import com.library.LibraryApp.core.repository.*;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class BookModelRepository implements BookRepository {

    private final BookR2dbcRepo bookR2dbcRepo;
    private final BookMapper bookMapper;
    private final AuthorR2dbcRepo authorR2dbcRepo;

    @Override
    public Mono<BookModel> save(BookModel newBook) {
        return bookR2dbcRepo.save(bookMapper.toEntity(newBook)).map(bookMapper::toModel);
    }

    @Override
    public Mono<Page<BookModel>> fetchBooks(SearchBookDto searchBookDto, Pageable pageable) {

        return authorR2dbcRepo.findAllByNameContaining(searchBookDto.getAuthor())
                .map(ProjectionId::getId)
                .collectList()
                .flatMap(authorIds -> {
                    Mono<List<BookModel>> booksMono = bookR2dbcRepo
                            .findByNameContainingAndUdkContainingAndAuthorIn(
                                    searchBookDto.getName(),
                                    searchBookDto.getUdk(),
                                    authorIds,
                                    pageable
                            )
                            .map(bookMapper::toModel)
                            .collectList();
                    Mono<Long> countMono = bookR2dbcRepo.count();
                    return Mono.zip(booksMono, countMono)
                            .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
                });

    }

    @Override
    public Mono<Void> delete(BookModel book) {
        return bookR2dbcRepo.delete(bookMapper.toEntity(book));
    }

    @Override
    public Mono<BookModel> findById(UUID id) {
        return bookR2dbcRepo.findById(id).map(bookMapper::toModel);
    }

    @Override
    public Mono<Long> countByAuthor(UUID id) {
        return bookR2dbcRepo.countByAuthor(id).map(ProjectionCount::getCount);

    }
}
