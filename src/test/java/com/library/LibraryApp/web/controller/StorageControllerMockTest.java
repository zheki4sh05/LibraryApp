package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.dto.BookState;
import com.library.LibraryApp.application.dto.CreateStorageDto;
import com.library.LibraryApp.application.dto.StorageDto;
import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.application.mapper.StorageMapper;
import com.library.LibraryApp.core.model.StorageModel;
import com.library.LibraryApp.core.service.impl.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.reactive.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.reactive.server.*;
import reactor.core.publisher.*;

import java.time.*;
import java.util.UUID;

import static org.mockito.Mockito.*;

@WebFluxTest(StorageController.class)
class StorageControllerMockTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LibraryStorageService storageService;

    @MockBean
    private StorageMapper storageMapper;

    UUID id = UUID.randomUUID();


    private CreateStorageDto createValidAdvancedDto() {
        return new CreateStorageDto(
                1,
                LocalDate.now().minusDays(1),
                BookState.FREE,
                id
        );
    }

    @Test
    void createStorage_WhenValidDto_ReturnsOk() {
        CreateStorageDto validDto = new CreateStorageDto(
                5,
                LocalDate.of(2024, 5, 25),
                BookState.BORROW,
                UUID.fromString("987e6543-e21a-12d3-a456-426614174111")
        );
        StorageModel entity = new StorageModel();
        StorageDto responseDto = new StorageDto(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                5,
                LocalDate.of(2024, 5, 25),
                BookState.BORROW,
                UUID.fromString("987e6543-e21a-12d3-a456-426614174111")
        );

        when(storageMapper.toNewModel(validDto)).thenReturn(entity);
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
                BookState.BORROW,
                null
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
        UUID validId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        when(storageService.deleteById(validId)).thenReturn(Mono.just(validId));

        webTestClient.delete()
                .uri("/storage/" + validId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class).isEqualTo(validId);
    }


    @Test
    void updateStorage_WhenValidAdvancedDto_ReturnsOk() {
        UUID id = UUID.randomUUID();
        CreateStorageDto requestDto = createValidAdvancedDto();
        StorageModel model = new StorageModel();
        StorageDto responseDto = new StorageDto(id, requestDto.getRack(), requestDto.getAccounting(), requestDto.getStatus(), requestDto.getEdition());

        when(storageMapper.toModel(requestDto,id)).thenReturn(model);
        when(storageService.update(model)).thenReturn(Mono.just(model));
        when(storageMapper.toDto(model)).thenReturn(responseDto);

        webTestClient.put()
                .uri("/storage/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectBody(StorageDto.class).isEqualTo(responseDto);
    }
    @Test
    void updateStorage_WhenMissingId_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                1,
                LocalDate.now().minusDays(1),
                BookState.FREE,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        );

        webTestClient.patch()
                .uri("/storage/ ")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }
    @Test
    void createStorage_InvalidRack_ReturnsBadRequest() {
        StorageDto invalidDto = new StorageDto(
                null,
                0,
                LocalDate.now().minusDays(1),
                BookState.FREE,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
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
                BookState.FREE,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
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
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
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
                .uri("/storage?mode=FREE&rack=5&from=2050-01-01")
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
        CreateStorageDto validDto = new CreateStorageDto(
                5,
                LocalDate.now().minusDays(1),
                BookState.FREE,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        );
        StorageDto storageDto = new StorageDto(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"), 5, LocalDate.now(), BookState.FREE, UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        when(storageMapper.toEntity(any())).thenReturn(new StorageEntity());
        when(storageService.update(any())).thenReturn(Mono.just(new StorageModel()));
        when(storageMapper.toDto(any())).thenReturn(storageDto);

        webTestClient.put()
                .uri("/storage/f47ac10b-58cc-4372-a567-0e02b2c3d479")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validDto)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void updateStorage_MissingId_ReturnsBadRequest() {
        CreateStorageDto invalidDto = new CreateStorageDto(
                5,
                LocalDate.now().minusDays(1),
                BookState.FREE,
                UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")
        );

        webTestClient.put()
                .uri("/storage/ ")
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().is4xxClientError();
    }


    @Test
    void deleteById_ValidUuid_ReturnsOk() {
        UUID validUuid = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        when(storageService.deleteById(validUuid)).thenReturn(Mono.just(validUuid));

        webTestClient.delete()
                .uri("/storage/" + validUuid)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class).isEqualTo(validUuid);
    }

    @Test
    void deleteById_InvalidUuid_ReturnsBadRequest() {
        webTestClient.delete()
                .uri("/storage/not-a-uuid")
                .exchange()
                .expectStatus().isBadRequest();
    }



}