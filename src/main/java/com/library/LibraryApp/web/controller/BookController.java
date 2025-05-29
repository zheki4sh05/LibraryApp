package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.dto.BookDto;
import com.library.LibraryApp.application.dto.CreateBookDto;
import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.application.mapper.BookMapper;
import com.library.LibraryApp.core.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public Mono<BookDto> save
            (
                  @Valid  @RequestBody
                  CreateBookDto bookDto
            ) {

       return bookService.save(bookMapper.toNewModel(bookDto)).map(bookMapper::toDto);
    }

    @GetMapping
    public Mono<Page<BookDto>> fetch(
          @Valid  SearchBookDto searchBookDto,
            Pageable pageable
    ){

        return bookService.fetch(searchBookDto,pageable)
                .map(page -> page.map(bookMapper::toDto));

    }

    @PutMapping("/{id}")
    public Mono<BookDto> update
        (@PathVariable UUID id,
         @Valid @RequestBody BookDto book) {
          return bookService.update(bookMapper.toModel(book, id)).map(bookMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<UUID> delete
            (@PathVariable UUID id) {
           return  bookService.delete(id);
    }


}
