package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.core.entity.*;
import com.library.LibraryApp.core.service.impl.*;
import com.library.LibraryApp.web.dto.*;
import com.library.LibraryApp.web.mapper.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.reactive.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.*;
import reactor.core.publisher.*;

import java.time.*;

import static org.mockito.Mockito.*;

@WebFluxTest(StorageController.class)
class StorageControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LibraryStorageService storageService;

    @MockBean
    private StorageMapper storageMapper;


    private StorageDto createValidAdvancedDto() {
        return new StorageDto(
                "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                1,
                LocalDate.now().minusDays(1),
                false,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );
    }

    @Test
    void createStorage_WhenValidDto_ReturnsOk() {
        StorageDto validDto = new StorageDto(
                null,
                5,
                LocalDate.of(2024, 5, 25),
                true,
                "987e6543-e21a-12d3-a456-426614174111"
        );
        Storage entity = new Storage();
        StorageDto responseDto = new StorageDto(
                "123e4567-e89b-12d3-a456-426614174000",
                5,
                LocalDate.of(2024, 5, 25),
                true,
                "987e6543-e21a-12d3-a456-426614174111"
        );

        when(storageMapper.toNewEntity(validDto)).thenReturn(entity);
        when(storageService.create(entity)).thenReturn(Mono.just(entity));
        when(storageMapper.toDto(entity)).thenReturn(responseDto);

        webTestClient.post()
                .uri("/storage")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StorageDto.class).isEqualTo(responseDto);
    }

    @Test
    void createStorage_WhenInvalidDto_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                5,
                LocalDate.of(2024, 5, 25),
                true,
                "invalid-id"
        );

        webTestClient.post()
                .uri("/storage")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Test
    void deleteById_WhenValidUuid_ReturnsOk() {
        String validId = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        when(storageService.deleteById(validId)).thenReturn(Mono.just(validId));

        webTestClient.delete()
                .uri("/storage/" + validId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(validId);
    }

    @Test
    void updateStorage_WhenValidAdvancedDto_ReturnsOk() {
        StorageDto requestDto = createValidAdvancedDto();
        Storage entity = new Storage();
        StorageDto responseDto = createValidAdvancedDto();

        when(storageMapper.toEntity(requestDto)).thenReturn(entity);
        when(storageService.update(entity)).thenReturn(Mono.just(entity));
        when(storageMapper.toDto(entity)).thenReturn(responseDto);

        webTestClient.patch()
                .uri("/storage")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StorageDto.class).isEqualTo(responseDto);
    }

    @Test
    void updateStorage_WhenMissingId_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                1,
                LocalDate.now().minusDays(1),
                false,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.patch()
                .uri("/storage")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }
    @Test
    void createStorage_InvalidRack_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                0,
                LocalDate.now().minusDays(1),
                false,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.post()
                .uri("/storage")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createStorage_FutureAccountingDate_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                1,
                LocalDate.now().plusDays(1),
                false,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.post()
                .uri("/storage")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createStorage_NullIsTaken_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                1,
                LocalDate.now().minusDays(1),
                null,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.post()
                .uri("/storage")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void fetch_MissingMode_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/storage?rack=5&from=2023-01-01")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void fetch_InvalidRack_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/storage?mode=AVAILABLE&rack=0&from=2023-01-01")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void fetch_FutureFromDate_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/storage?mode=AVAILABLE&rack=5&from=2050-01-01")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void findAllByEdition_InvalidUuid_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/storage/find?edition=invalid-uuid") // не UUID
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void findAllByEdition_MissingEdition_ReturnsBadRequest() {
        webTestClient.get()
                .uri("/storage/find") // edition обязателен
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateStorage_ValidDto_ReturnsOk() {
        StorageDto validDto = new StorageDto(
                "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                5,
                LocalDate.now().minusDays(1),
                false,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        when(storageMapper.toEntity(any())).thenReturn(new Storage());
        when(storageService.update(any())).thenReturn(Mono.just(new Storage()));
        when(storageMapper.toDto(any())).thenReturn(validDto);

        webTestClient.patch()
                .uri("/storage")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void updateStorage_MissingId_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                5,
                LocalDate.now().minusDays(1),
                false,
                "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        );

        webTestClient.patch()
                .uri("/storage")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void deleteById_ValidUuid_ReturnsOk() {
        String validUuid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        when(storageService.deleteById(validUuid)).thenReturn(Mono.just("Deleted"));

        webTestClient.delete()
                .uri("/storage/" + validUuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Deleted");
    }

    @Test
    void deleteById_InvalidUuid_ReturnsBadRequest() {
        webTestClient.delete()
                .uri("/storage/not-a-uuid")
                .exchange()
                .expectStatus().isBadRequest();
    }



}