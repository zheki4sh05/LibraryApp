package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.AbstractIntegrationTest;
import com.library.LibraryApp.core.entity.Author;
import com.library.LibraryApp.web.dto.AuthorDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class AuthorControllerTest extends AbstractIntegrationTest {

        @BeforeEach
        void clear_all(){
            authorRepository.deleteAll().subscribe();
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
        Author author = Author
                .builder()
                .name(name)
                .build();

        authorRepository.save(author).subscribe();

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
                .patch()
                .uri(AUTHOR_URI)
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
    public void update_not_existed_author_404(){
        var result1 = authorRepository.count();

        StepVerifier.create(result1)
                .assertNext(count->assertEquals(0, count))
                .verifyComplete();

        AuthorDto currentAuthorDto = AuthorDto.builder()
                .id(UUID.randomUUID().toString())
                .name("author")
                .build();
        webTestClient
                .patch()
                .uri(AUTHOR_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(currentAuthorDto)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class);


        var result2 = authorRepository.count();

        StepVerifier.create(result2)
                .assertNext(count->assertEquals(0, count))
                .verifyComplete();
    }

    @Test
    public void update_with_not_unique_name_409(){
         String name1 = "author";

        Author author = Author.builder()
                .name(name1)
                .build();
        authorRepository.save(author).subscribe();

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
                .expectBody(String.class)
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





}