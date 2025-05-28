package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.AbstractIntegrationTest;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.application.dto.AuthorDto;
import com.library.LibraryApp.application.dto.BookDto;

import com.library.LibraryApp.application.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookModelControllerTest extends AbstractIntegrationTest {


    @BeforeEach
    public void clearAll(){
        bookR2dbcRepo.deleteAll().subscribe();
        authorR2DbcRepo.deleteAll().subscribe();

    }


    @Test
    public void save_book_with_author_200(){

        AuthorDto authorDto = AuthorDto.builder()
                .name("author3")
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


        BookDto createBookDto = new BookDto(null, "book", "123.45.678", currentAuthorDto.getId());
        webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .consumeWith(bookDtoEntityExchangeResult -> {
                    var body = bookDtoEntityExchangeResult.getResponseBody();
                    assertNotNull(body);
                    assertNotNull(body.id());
                    assertEquals(createBookDto.name(), body.name());
                    assertEquals(currentAuthorDto.getId(), body.author());
                });

    }

    @Test
    public void save_book_with_not_existed_author_404(){
        BookDto createBookDto = new BookDto(null,"book", "123.45.678", UUID.randomUUID());
        webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .returnResult();

    }

    @Test
    public void save_book_with_not_unique_name_409(){
        String name = "book";
        String udk= "123.45.678";
        authorR2DbcRepo.deleteAll().subscribe();
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
        BookEntity book = BookEntity.builder()
                .name(name)
                .udk(udk)
                .author(currentAuthorDto.getId())
                .build();

        bookR2dbcRepo.save(book).subscribe();

        var createBookDto = new BookDto(null, name, udk, currentAuthorDto.getId());
        webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .returnResult();


    }

    @Test
    public void fetch_with_and_without_params_200(){
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

        var books = List.of(
          new BookEntity("book1", "621.391.83", currentAuthorDto.getId()),
          new BookEntity("book2", "621.391.83", currentAuthorDto.getId()),
          new BookEntity("book3", "621.391.83", currentAuthorDto.getId()),
          new BookEntity("book4", "621.391.83", currentAuthorDto.getId())
        );

        bookR2dbcRepo.saveAll(books).subscribe();

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(BOOK_URI)
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

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(BOOK_URI)
                                .queryParam("udk", "621.391.83")
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
                .jsonPath("$.content.length()").isEqualTo(4)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();



//        webTestClient
//                .get()
//                .uri(uriBuilder ->
//                        uriBuilder.path(BOOK_URI)
//                                .queryParam("udk", "621.391.83")
//                                .queryParam("name", "book2")
//                                .queryParam("page", 0)
//                                .queryParam("size", 10)
//                                .queryParam("sort", "name,asc")
//                                .build()
//
//                )
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isOk()
//                .expectHeader().contentType(MediaType.APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.content").isArray()
//                .jsonPath("$.content.length()").isEqualTo(1)
//                .jsonPath("$.size").isEqualTo(10)
//                .jsonPath("$.number").isEqualTo(0)
//                .jsonPath("$.totalPages").isNumber();


        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(BOOK_URI)
                                .queryParam("author",authorDto.getName())
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
                .jsonPath("$.content.length()").isEqualTo(4)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();

        webTestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(BOOK_URI)
                                .queryParam("author","unknown")
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
                .jsonPath("$.content.length()").isEqualTo(0)
                .jsonPath("$.size").isEqualTo(10)
                .jsonPath("$.number").isEqualTo(0)
                .jsonPath("$.totalPages").isNumber();


    }

    @Test
    public void update_existed_book_200(){

        AuthorDto authorDto = AuthorDto.builder()
                .name("author4")
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

        BookDto createBookDto = new BookDto(null, "book", "123.45.678", currentAuthorDto.getId());

      var result2 =   webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .returnResult();

      BookDto bookDto = result2.getResponseBody();
      assertNotNull(bookDto);
      BookDto updatedBook = new BookDto(bookDto.id(), "new name", "821.111", bookDto.author());

        webTestClient
                .put()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBook)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .consumeWith(bookDtoEntityExchangeResult -> {
                    var body = bookDtoEntityExchangeResult.getResponseBody();
                    assertNotNull(body);
                    assertEquals(updatedBook.id(), body.id());
                    assertEquals(updatedBook.name(), body.name());
                    assertEquals(updatedBook.udk(), body.udk());
                    assertEquals(updatedBook.author(), body.author());
                });

        var bookMono = bookR2dbcRepo.findById(updatedBook.id());

        StepVerifier.create(bookMono)
                .assertNext(book->{
                    assertEquals(updatedBook.name(), book.getName());
                    assertEquals(updatedBook.udk(), book.getUdk());
                })
                .verifyComplete();


    }

    @Test
    public void update_not_existed_book_404(){

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

        BookDto updatedBook = new BookDto(UUID.randomUUID(), "new name", "821.111", currentAuthorDto.getId());

        webTestClient
                .put()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBook)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class);

    }

    @Test
    public void update_existed_book_with_not_existed_author_404(){

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

        BookDto createBookDto = new BookDto(null, "book", "123.45.678", currentAuthorDto.getId());

        var result2 =   webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .returnResult();

        BookDto bookDto = result2.getResponseBody();
        assertNotNull(bookDto);
        BookDto updatedBook = new BookDto(bookDto.id(), "new name", "821.111", UUID.randomUUID());

        webTestClient
                .put()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBook)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class);

    }

    @Test
    public void delete_existed_book_200(){
        authorR2DbcRepo.deleteAll().subscribe();
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

        BookDto createBookDto = new BookDto(null, "book", "123.45.678", currentAuthorDto.getId());

        var result2 =   webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .returnResult();

        BookDto bookDto = result2.getResponseBody();
        assertNotNull(bookDto);

    var result3 =     webTestClient
                .delete()
                .uri(BOOK_URI+"/"+bookDto.id())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class)
                .returnResult();

    var bookId = result3.getResponseBody();
    assertNotNull(bookId);
    var bookMono = bookR2dbcRepo.findById(bookId);

    StepVerifier.create(bookMono)
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    public void delete_not_existed_book_404(){
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

        BookDto createBookDto = new BookDto(null, "book", "123.45.678", currentAuthorDto.getId());

        var result2 =   webTestClient
                .post()
                .uri(BOOK_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .returnResult();

        BookDto bookDto = result2.getResponseBody();
        assertNotNull(bookDto);

       webTestClient
                .delete()
                .uri(BOOK_URI+"/"+UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(String.class)
                .returnResult();


    }

}