package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.mapper.BookMapper;
import com.library.LibraryApp.core.service.impl.LibraryBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(BookController.class)
public class BookModelControllerMockTest {

    @MockBean
    LibraryBookService libraryBookService;

    @MockBean
    BookMapper bookMapper;

    @Autowired
    WebTestClient webTestClient;

    String BOOK_URI = "/book";


    @Test
    public void create_request_wrong_params_400(){

      String body = """
              {
                "id": null,
                "title": "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by...",
                "udk": "wrong-udk",
                "uuid": "wrong-uuid"
            }
            """;
        webTestClient.post().uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);
    }

    @Test
    public void test_fetch_wrong_params_400(){
        webTestClient.get().uri(uriBuilder ->
                        uriBuilder.queryParam("udk", "wrong-udk")
                                .build(BOOK_URI)
                        )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .queryParam("name", "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
                                .build(BOOK_URI)
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

        webTestClient.get().uri(uriBuilder ->
                        uriBuilder
                                .queryParam("author", "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")
                                .build(BOOK_URI)
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);
    }

    @Test
    public void update_with_wrong_params_400(){

      String body = """
               {
                          "id": null,
                              "title": "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized...",
                              "udk": "wrong-udk",
                              "uuid": "wrong-uuid"
                      }
              
              """;

        webTestClient.put().uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

    }

    @Test
    public void delete_by_wrong_id_400(){
        webTestClient.get().uri(BOOK_URI+"/"
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);

        webTestClient.get().uri(BOOK_URI+"/wrong-id"
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class);
    }


}
