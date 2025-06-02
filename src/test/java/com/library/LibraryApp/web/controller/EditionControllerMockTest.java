package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.dto.CreateEditionDto;
import com.library.LibraryApp.application.dto.EditionDto;
import com.library.LibraryApp.application.mapper.EditionMapper;
import com.library.LibraryApp.core.model.EditionModel;
import com.library.LibraryApp.core.service.impl.BookEditionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebFluxTest(EditionController.class)
class EditionControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    BookEditionService editionService;


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
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        );

        when(editionService.create(any())).thenReturn(Mono.just(new EditionModel()));
        when(editionMapper.toDto(any())).thenReturn(new EditionDto(null, "", 1, LocalDate.now(), 1, UUID.randomUUID()));

        webTestClient.post()
                .uri("/edition")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void createEdition_WhenInvalidIsbn_ReturnsBadRequest() {
        CreateEditionDto invalidDto = new CreateEditionDto(
                "INVALID-ISBN",
                200,
                LocalDate.now().minusDays(1),
                1,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
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
        CreateEditionDto invalidDto = new CreateEditionDto(
                "978-3-16-148410-0",
                0,
                LocalDate.now().minusDays(1),
                1,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479") // Оборачиваем в UUID.fromString()
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
        UUID validId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(editionService.findById(validId)).thenReturn(Mono.just(new EditionModel()));
        when(editionMapper.toDto(any())).thenReturn(new EditionDto(null, "", 1, LocalDate.now(), 1, UUID.randomUUID()));

        webTestClient.get()
                .uri("/edition/id/" + validId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getEditionById_WhenInvalidUuid_ReturnsBadRequest() {
        String invalidId = "NOT-A-UUID";

        webTestClient.get()
                .uri("/edition/id/" + invalidId)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getEditionById_WhenBlankId_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/edition/id/")
                .exchange()
                .expectStatus().is4xxClientError();
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
    void fetch_WhenNameIsTooLong_ReturnsBadRequest() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/edition/fetch")
                        .queryParam("name", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
                        .build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateEdition_WhenValidDto_ReturnsOk() {
        CreateEditionDto validDto = new CreateEditionDto(

                "9783161484100",
                200,
                LocalDate.now().minusDays(1),
                1,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        );

        when(editionService.update(any())).thenReturn(Mono.just(new EditionModel()));
        when(editionMapper.toDto(any())).thenReturn(new EditionDto(null, "", 1, LocalDate.now(), 1, UUID.randomUUID()));

        webTestClient.put()
                .uri("/edition"+"/"+ "f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void updateEdition_WhenMissingId_ReturnsBadRequest() {
        CreateEditionDto invalidDto = new CreateEditionDto(
                "9783161484100",
                200,
                LocalDate.now().minusDays(1),
                1,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479") // Оборачиваем в UUID
        );

        webTestClient.put()
                .uri("/edition ")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void deleteEdition_WhenValidId_ReturnsOk() {
        UUID validId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"); // Используем UUID

        when(editionService.delete(validId)).thenReturn(Mono.just(validId));

        webTestClient.delete()
                .uri("/edition/" + validId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var body = stringEntityExchangeResult.getResponseBody();
                    assertEquals(validId, body);
                });
    }


    @Test
    void deleteEdition_WhenBlankId_ReturnsBadRequest() {
        webTestClient.delete()
                .uri("/edition/")
                .exchange()
                .expectStatus().is4xxClientError();
    }


}