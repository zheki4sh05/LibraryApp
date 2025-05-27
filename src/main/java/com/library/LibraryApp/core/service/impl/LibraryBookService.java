package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.model.BookModel;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.core.repository.BookRepository;
import com.library.LibraryApp.core.util.SqlQueryFactoryUtil;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.AuthorR2dbcRepo;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.FetchQueries;
import com.library.LibraryApp.core.service.BookService;
import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.BookR2dbcRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class LibraryBookService implements BookService {


    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final FetchQueries fetchQueries;


    public Mono<BookModel> save(BookModel newBook) {

      return authorRepository.findById(newBook.getAuthor())
              .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id "+newBook.getAuthor())))
              .flatMap(author -> bookRepository.save(newBook));

    }
    @Override
    public Mono<Page<BookModel>> fetch(SearchBookDto searchBookDto, Pageable pageable) {
        return bookRepository.fetchBooks( searchBookDto,  pageable);
//        int size = pageable.getPageSize();
//        int offset = SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), size);
//        String sql = SqlQueryFactoryUtil.createBookQuery(pageable);
//        Flux<BookModel> authors = fetchQueries.fetchBooks(sql, searchBookDto, size, offset);
//        return authors.collectList()
//                .zipWith(bookR2dbcRepo.count())
//                .map(tuple->
//                        new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())
//                );

    }





    public Mono<BookModel> update(BookModel book) {
        return authorRepository.findById(book.getAuthor())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id "+book.getAuthor().toString())))
                .flatMap(author -> bookRepository.save(book));
    }

    public Mono<UUID> delete(UUID id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти книгу по id " + id)))
                .flatMap(book -> bookRepository.delete(book).thenReturn(id));
    }

}
