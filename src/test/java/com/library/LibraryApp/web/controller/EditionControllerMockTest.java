package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.core.entity.*;
import com.library.LibraryApp.core.repository.*;
import com.library.LibraryApp.core.service.impl.*;
import com.library.LibraryApp.web.dto.*;
import com.library.LibraryApp.web.mapper.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.reactive.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.*;
import reactor.core.publisher.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.*;
import java.util.*;

import static org.mockito.Mockito.*;

@WebFluxTest(EditionController.class)
class EditionControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    BookEditionService editionService;

    @MockBean
    EditionRepository editionRepository;

    @MockBean
    EditionMapper editionMapper;

    @Test
    void createEdition_WhenValidDto_ReturnsOk() {
        EditionDto validDto = new EditionDto(
                null,
                "9783161484100",
                200,
                LocalDate.now().minusDays(1),
                1,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        when(editionService.create(any())).thenReturn(Mono.just(new Edition()));
        when(editionMapper.toDto(any())).thenReturn((new EditionDto("", "",1, LocalDate.now(), 1, UUID.randomUUID().toString())));

        webTestClient.post()
                .uri("/edition")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createEdition_WhenInvalidIsbn_ReturnsBadRequest() {
        EditionDto invalidDto = new EditionDto(
                null,
                "INVALID-ISBN",
                200,
                LocalDate.now().minusDays(1),
                1,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.post()
                .uri("/edition")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createEdition_WhenPagesOutOfRange_ReturnsBadRequest() {
        EditionDto invalidDto = new EditionDto(
                null,
                "978-3-16-148410-0",
                0,
                LocalDate.now().minusDays(1),
                1,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.post()
                .uri("/edition")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getEditionById_WhenValidUuid_ReturnsOk() {
        String validId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        when(editionService.findById(validId)).thenReturn(Mono.just(new Edition()));
        when(editionMapper.toDto(any())).thenReturn((new EditionDto("", "",1, LocalDate.now(), 1, UUID.randomUUID().toString())));
        webTestClient.get()
                .uri("/edition/" + validId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getEditionById_WhenInvalidUuid_ReturnsBadRequest() {
        String invalidId = "NOT-A-UUID";

        webTestClient.get()
                .uri("/edition/" + invalidId)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getEditionById_WhenBlankId_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/edition/ ")
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void fetch_WhenInvalidPublicationDate_ReturnsBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/edition/fetch")
                        .queryParam("publication", "2050-01-01")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void fetch_WhenNumberOutOfRange_ReturnsBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/edition/fetch")
                        .queryParam("number", "0")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void fetch_WhenNameISTooLong_ReturnsBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/edition/fetch")
                        .queryParam("name", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateEdition_WhenValidDto_ReturnsOk() {
        EditionDto validDto = new EditionDto(
                "f47ac10b-58cc-4372-a567-0e02b2c3d479", // валидный UUID (обязателен для обновления)
                "9783161484100",
                200,
                LocalDate.now().minusDays(1),
                1,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        when(editionService.update(any())).thenReturn(Mono.just(new Edition()));
        when(editionMapper.toDto(any())).thenReturn((new EditionDto("", "",1, LocalDate.now(), 1, UUID.randomUUID().toString())));
        webTestClient.put()
                .uri("/edition")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateEdition_WhenMissingId_ReturnsBadRequest() {
        EditionDto invalidDto = new EditionDto(
                null,
                "9783161484100",
                200,
                LocalDate.now().minusDays(1),
                1,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.put()
                .uri("/edition")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deleteEdition_WhenValidId_ReturnsOk() {
        String validId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        when(editionService.delete(validId)).thenReturn(Mono.just(validId));

        webTestClient.delete()
                .uri("/edition/" + validId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                   var body = stringEntityExchangeResult.getResponseBody();
                   assertEquals(validId, body);
                });
    }

    @Test
    void deleteEdition_WhenBlankId_ReturnsBadRequest() {
        webTestClient.delete()
                .uri("/edition/ ")
                .exchange()
                .expectStatus().isBadRequest();
    }


}