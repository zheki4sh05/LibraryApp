package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.AbstractIntegrationTest;
import com.library.LibraryApp.application.dto.CreateEditionDto;
import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.application.dto.EditionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EditionControllerTest extends AbstractIntegrationTest {

    @BeforeEach
    public void clearAll(){
        editionRepository.deleteAll().subscribe();
        bookR2dbcRepo.deleteAll().subscribe();
        authorR2DbcRepo.deleteAll().subscribe();

    }

    @Test
    public void create_edition_200(){
        var currentAuthorDto = createAuthor();

      var savedBook = createBookDto(currentAuthorDto.getId());


        EditionDto edition = new EditionDto(
                savedBook.id(),
                "9780123456789",
                300,
                LocalDate.of(2020, 5, 15),
                5,
                savedBook.id()
        );


      var editionResponseBody =   webTestClient
                .post()
                .uri(EDITION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(edition)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(EditionDto.class)
              .returnResult();

        var savedEdition = editionResponseBody.getResponseBody();

        assertNotNull(savedEdition);
        assertEquals(edition.isbn(), savedEdition.isbn());


    }

    @Test
    public void find_by_id_200(){
        var edition = createEdition();
      webTestClient
                .get()
                .uri(EDITION_URI+"/"+edition.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody(EditionDto.class)
                .consumeWith(editionDtoEntityExchangeResult -> {
                   var body = editionDtoEntityExchangeResult.getResponseBody();
                   assertNotNull(body);
                   assertEquals(body.id(), edition.id());
                });
    }

    @Test
    public void not_found_by_id_404(){
        webTestClient
                .get()
                .uri(EDITION_URI+"/"+ UUID.randomUUID().toString())
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(String.class)
                .returnResult();
    }

    @Test
    public void fetch_with_params_200(){
        var author = createAuthor();
        var book = createBookDto(author.getId());

        List<EditionEntity> editions = List.of(
                new EditionEntity( "9780123456789", 250, LocalDate.of(2019, 6, 10),  1,book.id()),
                new EditionEntity( "9791234567890", 320, LocalDate.of(2020, 2, 5), 2, book.id()),
                new EditionEntity( "9782123456789", 400, LocalDate.of(2021, 11, 30), 3, book.id())
        );

        editionRepository.saveAll(editions).subscribe();

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(EDITION_URI+"/fetch")
                                .queryParam("number", 1)
                                .queryParam("publication", "2019-06-10")
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
                .jsonPath("$.content.length()").isEqualTo(1)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();

    }



    @Test
    public void update_edition_200(){
        var edition = createEdition();
        CreateEditionDto edition1 = new CreateEditionDto("9781123456789", edition.pages(), edition.publication(), edition.number(), edition.book());
       var result =  webTestClient
                .put()
                .uri(EDITION_URI+"/"+edition.id())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(edition1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(EditionDto.class)
               .returnResult();


       var body = result.getResponseBody();
        assertNotNull(body);
        assertNotEquals(edition.isbn(), body.isbn());
        assertEquals(edition.id(), body.id());



    }

    @Test
    public void update_not_existed_edition_404(){
        var author = createAuthor();
        var book = createBookDto(author.getId());
        CreateEditionDto edition1 = new CreateEditionDto( "9781123456789", 1, LocalDate.parse("1970-09-09"), 1, book.id());
      webTestClient
                .put()
                .uri(EDITION_URI+"/"+UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(edition1)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class);
    }

    @Test
    public void update_with_not_existed_book_404(){
        var edition = createEdition();
        CreateEditionDto edition1 = new CreateEditionDto("9781123456789", 1, LocalDate.parse("1970-09-09"), 1, UUID.randomUUID());
        webTestClient
                .put()
                .uri(EDITION_URI+"/"+edition.id())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(edition1)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class);
    }

    @Test
    public void delete_without_storage_200(){
        var edition = createEdition();
        webTestClient
                .delete()
                .uri(EDITION_URI+"/"+edition.id())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class);
    }


}





