package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.core.service.impl.LibraryBookService;
import com.library.LibraryApp.application.dto.BookDto;
import com.library.LibraryApp.web.mapper.BookMapper;
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

        BookDto bookDto = new BookDto(null, "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selectio", "wrong-udk", "wrong-uuid");

        webTestClient.post().uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
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

        BookDto bookDto = new BookDto(null, "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selectio", "wrong-udk", "wrong-uuid");

        webTestClient.put().uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(bookDto)
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
