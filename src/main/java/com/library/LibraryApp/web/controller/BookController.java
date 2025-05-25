package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.core.service.BookService;
import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.dto.BookDto;
import com.library.LibraryApp.web.dto.SearchBookDto;
import com.library.LibraryApp.web.mapper.BookMapper;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/book")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public Mono<BookDto> save
            (@RequestBody
             @Validated(BasicInfo.class) BookDto bookDto
            ) {

       return bookService.save(bookMapper.toNewEntity(bookDto)).map(bookMapper::toDto);
    }

    @GetMapping
    public Mono<Page<BookDto>> fetch(
            @RequestParam(value = "udk", required = false, defaultValue = "")  String udk,
            @RequestParam(value = "name", required = false, defaultValue = "")  String name,
            @RequestParam(value = "author", required = false, defaultValue = "")  String author,
            Pageable pageable
    ){

        SearchBookDto searchBookDto = new SearchBookDto(udk, name, author);
        return bookService.fetch(searchBookDto,pageable)
                .map(page -> page.map(bookMapper::toDto));

    }

    @PutMapping
    public Mono<BookDto> update
            (@RequestBody
             @Validated(AdvancedInfo.class) BookDto book) {
          return bookService.update(bookMapper.toEntity(book)).map(bookMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete
            (@PathVariable
             @Pattern(regexp = RegexPatterns.UUID) String id) {
           return  bookService.delete(id);
    }


}
