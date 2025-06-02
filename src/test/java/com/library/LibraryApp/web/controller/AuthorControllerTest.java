package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.AbstractIntegrationTest;
import com.library.LibraryApp.application.dto.CreateBookDto;
import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.application.dto.AuthorDto;
import com.library.LibraryApp.application.entity.BookEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class AuthorControllerTest extends AbstractIntegrationTest {

        @BeforeEach
        void clear_all(){
            authorR2DbcRepo.deleteAll().subscribe();
        }

    @Test
    public void create_author_200(){
        AuthorDto createAuthorDto = AuthorDto.builder()
                .name("author")
                .build();
        webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createAuthorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .consumeWith(response->{
                    var body = response.getResponseBody();
                    assertNotNull(body);
                    assertEquals(createAuthorDto.getName(), body.getName());
                    assertNotNull(body.getId());
                });

    }

    @Test
    public void create_author_with_existed_name_409(){
        final String name="автор";
        AuthorEntity author = AuthorEntity
                .builder()
                .name(name)
                .build();

        authorR2DbcRepo.save(author).subscribe();

        AuthorDto createAuthorDto = AuthorDto.builder()
                .name(name)
                .build();
        webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createAuthorDto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .consumeWith(response->{
                    var body = response.getResponseBody();
                    assertNotNull(body);
                });

    }

    @Test
    public void find_by_id_existed_author_200(){
       AuthorDto authorDto = AuthorDto.builder()
               .name("author")
               .build();
               var result = webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .returnResult();

        var currentAuthorDto = result.getResponseBody();
       webTestClient
        .get()
        .uri(AUTHOR_URI+"/"+currentAuthorDto.getId())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(AuthorDto.class)
               .consumeWith(authorDtoEntityExchangeResult -> {
                   var body = authorDtoEntityExchangeResult.getResponseBody();
                   assertNotNull(body);
                   assertEquals(currentAuthorDto.getId(), body.getId());
                   assertEquals(currentAuthorDto.getName(), body.getName());
               });
    }

    @Test
    public void find_not_existed_author_404(){
        webTestClient
                .get()
                .uri(AUTHOR_URI+"/"+UUID.randomUUID())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .returnResult();
    }

    @Test
    public void update_existed_author_200(){
        AuthorDto authorDto = AuthorDto.builder()
                .name("author")
                .build();
        var result = webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .returnResult();

        var currentAuthorDto = result.getResponseBody();
        assertNotNull(currentAuthorDto);
        String newName = "author";
        currentAuthorDto.setName(newName);

                webTestClient
                .put()
                .uri(AUTHOR_URI+"/"+currentAuthorDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(currentAuthorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .consumeWith(authorDtoEntityExchangeResult -> {
                    var body = authorDtoEntityExchangeResult.getResponseBody();
                    assertNotNull(body);
                     assertEquals(currentAuthorDto.getId(), body.getId());
                     assertEquals(newName, body.getName());
                });
    }

    @Test
    public void update_not_existed_author_404() {

        Mono<Long> initialCount = authorR2DbcRepo.count().cache();

        AuthorDto currentAuthorDto = AuthorDto.builder()
                .id(UUID.randomUUID())
                .name("author")
                .build();


        webTestClient
                .put()
                .uri(AUTHOR_URI + "/" + currentAuthorDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(currentAuthorDto)
                .exchange()
                .expectStatus().isNotFound() // 404
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class);


        StepVerifier.create(initialCount.zipWith(authorR2DbcRepo.count()))
                .assertNext(tuple -> {
                    Long beforeCount = tuple.getT1();
                    Long afterCount = tuple.getT2();
                    assertEquals(beforeCount, afterCount);
                })
                .verifyComplete();
    }

    @Test
    public void update_with_not_unique_name_409(){
         String name1 = "author";

        AuthorEntity author = AuthorEntity.builder()
                .name(name1)
                .build();
        authorR2DbcRepo.save(author).subscribe();

        AuthorDto authorDto = AuthorDto.builder()
                .name("author2")
                .build();
        var result = webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .returnResult();

        var currentAuthorDto = result.getResponseBody();

        assertNotNull(currentAuthorDto);

        currentAuthorDto.setName(name1);

        webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .returnResult();
    }

    @Test
    public void delete_by_id_existed_author_200(){

        AuthorDto authorDto = AuthorDto.builder()
                .name("author2")
                .build();
        var result = webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .returnResult();

        var currentAuthorDto = result.getResponseBody();

        assertNotNull(currentAuthorDto);

        webTestClient
                .delete()
                .uri(AUTHOR_URI+"/"+currentAuthorDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class)
                .consumeWith(authorDtoEntityExchangeResult -> {
                    var body = authorDtoEntityExchangeResult.getResponseBody();
                    assertNotNull(body);
                    assertEquals(currentAuthorDto.getId(), body);
                });



    }
    @Test
    public void delete_by_id_not_existed_author_404(){

        webTestClient
                .delete()
                .uri(AUTHOR_URI+"/"+UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(String.class)
                .returnResult();

    }

    @Test
    public void delete_author_with_existed_book_409(){
        AuthorDto authorDto = AuthorDto.builder()
                .name("author2")
                .build();
        var result = webTestClient
                .post()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(authorDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(AuthorDto.class)
                .returnResult();

        var currentAuthorDto = result.getResponseBody();

        assertNotNull(currentAuthorDto);

        BookEntity book = new BookEntity("book", "27406569652230375886", currentAuthorDto.getId());
        bookR2dbcRepo.save(book).subscribe();

        webTestClient
                .delete()
                .uri(AUTHOR_URI+"/"+currentAuthorDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(String.class)
                .returnResult();

    }

    @Test
    public void fetch_authors_by_params_200(){
        StepVerifier.create(
                        authorR2DbcRepo.deleteAll()
                                .thenMany(authorR2DbcRepo.saveAll(getAuthorEntities()))
                                .then()
                )
                .expectComplete()
                .verify();
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(AUTHOR_URI)
                                .queryParam("name","author")
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
                .jsonPath("$.content.length()").isEqualTo(5)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();
    }


    @Test
    public void fetch_authors_empty_name_params_200(){

        StepVerifier.create(
                        authorR2DbcRepo.deleteAll()
                                .thenMany(authorR2DbcRepo.saveAll(getAuthorEntities()))
                                .then()
                )
                .expectComplete()
                .verify();
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(AUTHOR_URI)
                                .queryParam("name","")
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
                .jsonPath("$.content.length()").isEqualTo(getAuthorEntities().size())
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();

    }


    @Test
    public void fetch_authors_check_page_with_params_200(){
        StepVerifier.create(
                        authorR2DbcRepo.deleteAll()
                                .thenMany(authorR2DbcRepo.saveAll(getAuthorEntities()))
                                .then()
                )
                .expectComplete()
                .verify();
        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(AUTHOR_URI)
                                .queryParam("name","author")
                                .queryParam("page", 0)
                                .queryParam("size", 2)
                                .queryParam("sort", "name,asc")
                                .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.content").isArray()
                .jsonPath("$.content.length()").isEqualTo(2)
                .jsonPath("$.size").isEqualTo(2)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();
    }

    private List<AuthorEntity> getAuthorEntities(){
            return List.of(
                    new AuthorEntity(null, "author1"),
                    new AuthorEntity(null, "author2"),
                    new AuthorEntity(null, "author3"),
                    new AuthorEntity(null, "author4"),
                    new AuthorEntity(null, "author5"),
                    new AuthorEntity(null, "123456"),
                    new AuthorEntity(null, "автор"),
                    new AuthorEntity(null, "имяавтора")
            );
    }

}