package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.AuthorR2dbcRepo;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.FetchQueries;
import com.library.LibraryApp.application.dto.SearchAuthorDto;
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
class BookModelAuthorServiceTest {

    @Mock
    AuthorR2dbcRepo authorR2DbcRepo;

    @Mock
    FetchQueries fetchQueries;

    @InjectMocks
    BookAuthorService bookAuthorService;

    @Test
    public void fetch_list(){
        List<AuthorEntity> authors = List.of(
                new AuthorEntity(UUID.randomUUID(), "Лев Толстой"),
                new AuthorEntity(UUID.randomUUID(), "Фёдор Достоевский"),
                new AuthorEntity(UUID.randomUUID(), "Антон Чехов")
        );

        Flux<AuthorEntity> authorFlux = Flux.fromIterable(authors);

        SearchAuthorDto searchAuthorDto = new SearchAuthorDto("");

        when((authorR2DbcRepo.count())).thenReturn(Mono.just(3L));
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