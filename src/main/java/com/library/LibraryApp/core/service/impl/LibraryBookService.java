package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.util.SqlQueryFactoryUtil;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.core.repository.FetchQueries;
import com.library.LibraryApp.core.service.BookService;
import com.library.LibraryApp.core.entity.Book;
import com.library.LibraryApp.web.dto.SearchBookDto;
import com.library.LibraryApp.web.exceptions.BadRequestException;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import com.library.LibraryApp.core.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LibraryBookService implements BookService {


    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final FetchQueries fetchQueries;


    public Mono<Book> save(Book newBook) {

      return authorRepository.findById(newBook.getAuthor())
              .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id "+newBook.getAuthor())))
              .flatMap(author -> bookRepository.save(newBook));

    }
    @Override
    public Mono<Page<Book>> fetch(SearchBookDto searchBookDto, Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), size);
        String sql = SqlQueryFactoryUtil.createBookQuery(pageable);
        Flux<Book> authors = fetchQueries.fetchBooks(sql, searchBookDto, size, offset);
        return authors.collectList()
                .zipWith(bookRepository.count())
                .map(tuple->
                        new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())
                );

    }




    public Mono<Book> update(Book book) {
       return authorRepository.findById(book.getAuthor())
               .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id "+book.getAuthor().toString())))
               .flatMap(author -> bookRepository.save(book));
    }

    public Mono<String> delete(String number) {
        return bookRepository.findByNumber(UUID.fromString(number))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти книгу по id "+number)))
                .flatMap(book -> bookRepository.delete(book).thenReturn(number));
    }

}
