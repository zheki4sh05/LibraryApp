package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.dto.CreateAuthorDto;
import com.library.LibraryApp.application.mapper.AuthorMapper;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.AuthorR2dbcRepo;
import com.library.LibraryApp.core.service.impl.BookAuthorService;
import com.library.LibraryApp.application.dto.AuthorDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;


@WebFluxTest(AuthorController.class)
public class AuthorControllerMockTest {

   @MockBean
    BookAuthorService authorService;

   @MockBean
   AuthorR2dbcRepo authorR2DbcRepo;

    @MockBean
    AuthorMapper authorMapper;

    @Autowired
    private WebTestClient webTestClient;


    String author_uri = "/author";


    @Test
    public void test_author_create_with_wrong_bodies_values_400() {

        String body = """
                {
                    name: "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selectio"
                }
                """;
        CreateAuthorDto createAuthorDto = CreateAuthorDto.builder()
                .name(body)
                .build();

        webTestClient.post().uri(author_uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createAuthorDto)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

    }

    @Test
    public void test_find_by_id_400(){

        webTestClient.get().uri(author_uri+"/wrong-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

        webTestClient.get().uri(author_uri+"/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

    }

    @Test
    public void test_update_400(){
        String author = """
                {
                  "id": "wrong-id",
                  "name": "Иван Иванов"
                }
                """;

        webTestClient.put().uri(author_uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(author)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

        AuthorDto createAuthorDto2 = new AuthorDto(UUID.randomUUID(), "123");

        webTestClient.put().uri(author_uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createAuthorDto2)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

    }

    @Test
    public void test_delete_400(){
        webTestClient.delete().uri(author_uri+"/wrong-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

        webTestClient.delete().uri(author_uri+"/")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);
    }





}
