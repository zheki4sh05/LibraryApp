package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.entity.Author;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.core.repository.FetchQueries;
import com.library.LibraryApp.core.util.SqlQueryFactoryUtil;
import com.library.LibraryApp.web.dto.SearchAuthorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookAuthorServiceTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    FetchQueries fetchQueries;

    @InjectMocks
    BookAuthorService bookAuthorService;

    @Test
    public void fetch_list(){
        List<Author> authors = List.of(
                new Author(UUID.randomUUID(), "Лев Толстой"),
                new Author(UUID.randomUUID(), "Фёдор Достоевский"),
                new Author(UUID.randomUUID(), "Антон Чехов")
        );

        Flux<Author> authorFlux = Flux.fromIterable(authors);

        SearchAuthorDto searchAuthorDto = new SearchAuthorDto("");

        when((authorRepository.count())).thenReturn(Mono.just(3L));
        when(fetchQueries.fetchAuthors(any(String.class), any(SearchAuthorDto.class), any(Integer.class), any(Integer.class))).thenReturn(authorFlux);

        Pageable pageable = PageRequest.of(0, 10);

        var result = bookAuthorService.fetch(searchAuthorDto, pageable);

        StepVerifier.create(result)
                .assertNext(page->{
                        assertEquals(3, page.getTotalElements());
                        assertEquals(1, page.getTotalPages());
                        assertEquals(0, page.getNumber());
                })
                .expectComplete();

    }



}