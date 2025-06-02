package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.model.BookModel;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.core.repository.BookRepository;
import com.library.LibraryApp.core.service.BookService;
import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class LibraryBookService implements BookService {


    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;



    public Mono<BookModel> save(BookModel newBook) {

      return authorRepository.findById(newBook.getAuthor())
              .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id "+newBook.getAuthor())))
              .flatMap(author -> bookRepository.save(newBook));

    }
    @Override
    public Mono<Page<BookModel>> fetch(SearchBookDto searchBookDto, Pageable pageable) {
        log.info(searchBookDto.toString());
        return bookRepository.fetchBooks( searchBookDto,  pageable);
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
