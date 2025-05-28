package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.AbstractIntegrationTest;
import com.library.LibraryApp.application.entity.StorageEntity;
import com.library.LibraryApp.application.dto.BookState;
import com.library.LibraryApp.application.dto.StorageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StorageControllerTest extends AbstractIntegrationTest {


    @BeforeEach
    public void clearAll(){
        storageRepository.deleteAll().subscribe();
        editionRepository.deleteAll().subscribe();
        bookR2dbcRepo.deleteAll().subscribe();
        authorR2DbcRepo.deleteAll().subscribe();

    }

    @Test
    public void create_storage_200(){
        var edition = createEdition();

        StorageDto storageDto = new StorageDto(edition.id(), 1, LocalDate.now(), BookState.FREE, edition.id());

        webTestClient
                .post()
                .uri(STORAGE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(storageDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StorageDto.class)
                .consumeWith(storageDtoEntityExchangeResult -> {
                    var body = storageDtoEntityExchangeResult.getResponseBody();
                    assertNotNull(body);
                    assertNotNull(body.id());
                    assertEquals(storageDto.edition(), body.edition());

                });

    }

    @Test
    public void fetch_with_params_200(){
        var edition = createEdition();
        List<StorageEntity> storages = List.of(
                new StorageEntity(null, 5, LocalDate.of(2023, 6, 10), BookState.FREE, edition.id()),
                new StorageEntity(null, 12, LocalDate.of(2024, 2, 15), BookState.BORROW, edition.id()),
                new StorageEntity(null, 5, LocalDate.of(2022, 11, 30), BookState.FREE, edition.id()),
                new StorageEntity(null, 3, LocalDate.of(2021, 8, 20), BookState.BORROW, edition.id()),
                new StorageEntity(null, 15, LocalDate.of(2020, 5, 5), BookState.FREE, edition.id()),
                new StorageEntity(null, 9, LocalDate.of(2023, 11, 1), BookState.BORROW, edition.id()),
                new StorageEntity(null, 6, LocalDate.of(2024, 4, 7), BookState.FREE, edition.id())
        );


        storageRepository.saveAll(storages).subscribe();

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(STORAGE_URI)
                                .queryParam("status", BookState.FREE)
                                .queryParam("dateFrom", "1970-01-01")
                                .queryParam("dateTo", LocalDate.now().toString())
                                .queryParam("rack", 5)
                                .queryParam("page", 0)
                                .queryParam("size", 10)
                                .queryParam("sort", "name,asc")
                                .build()

                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();

    }


    @Test
    public void find_by_edition_id_200(){
        var edition = createEdition();
        List<StorageEntity> storages = List.of(
                new StorageEntity(null, 5, LocalDate.of(2023, 6, 10), BookState.BORROW, edition.id()),
                new StorageEntity(null, 5, LocalDate.of(2024, 2, 15), BookState.BORROW, edition.id()),
                new StorageEntity(null, 5, LocalDate.of(2022, 11, 30), BookState.BORROW, edition.id())
        );

        storageRepository.saveAll(storages).subscribe();

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(STORAGE_URI+"/find")
                                .queryParam("edition", edition.id())
                                .queryParam("page", 0)
                                .queryParam("size", 10)
                                .queryParam("sort", "name,asc")
                                .build()

                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(3)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();



    }


    @Test
    public void delete_by_id_200(){
        var edition = createEdition();

        StorageDto storageDto = new StorageDto(null, 1, LocalDate.now(), BookState.FREE,  edition.id());
      var savedStorage =  webTestClient
                .post()
                .uri(STORAGE_URI)
                .bodyValue(storageDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StorageDto.class)
                        .returnResult().getResponseBody();
        assertNotNull(savedStorage);

        webTestClient
                .delete()
                .uri(STORAGE_URI+"/"+savedStorage.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var body = stringEntityExchangeResult.getResponseBody();
                    assertEquals(savedStorage.id(), body);
                });


    }

    @Test
    public void update_storage_200(){
        var storage = createStorage();
        StorageDto storageDto = new StorageDto(storage.id(), 2, LocalDate.now(), BookState.FREE, storage.edition());
        var savedStorage =  webTestClient
                .put()
                .uri(STORAGE_URI+"/"+storage.id())
                .bodyValue(storageDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StorageDto.class)
                .returnResult().getResponseBody();

        assertNotNull(savedStorage);
        assertEquals(storage.id(),savedStorage.id());

        var monoStorage = storageRepository.findById(storage.id());
        StepVerifier.create(monoStorage)
                .assertNext(storage1->{
                    assertEquals(storageDto.rack(), storage1.getRack());
                    assertEquals(storageDto.edition(), storage1.getEdition());
                    assertEquals(storageDto.status(), storage1.getStatus());
                })
                .verifyComplete();

    }


}