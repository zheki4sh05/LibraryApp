package com.library.LibraryApp;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.AuthorR2dbcRepo;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.BookR2dbcRepo;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.EditionR2dbcRepository;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.StorageR2dbcRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = LibraryAppApplication.class)
@AutoConfigureWebTestClient
@Slf4j
@Testcontainers
public class AbstractIntegrationTest {

    @Autowired
    protected BookR2dbcRepo bookR2dbcRepo;


    @Autowired
    public AuthorR2dbcRepo authorR2DbcRepo;

    @Autowired
    public EditionR2dbcRepository editionRepository;

    @Autowired
    public StorageR2dbcRepository storageRepository;


    @Autowired
    protected WebTestClient webTestClient;

    public final String BOOK_URI = "/book";
    public final String AUTHOR_URI = "/author";
    public final String EDITION_URI = "/edition";
    public final String STORAGE_URI="/storage";

//    @Container
     static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withUsername("postgres")
            .withPassword("postgres")
             .withDatabaseName("postgres")
            .withReuse(true);

     @BeforeAll
     static void startContainer(){
         postgreSQLContainer.start();
     }





    @DynamicPropertySource
    protected static void dynamicPropertiesProperties(DynamicPropertyRegistry registry) {
        log.info(postgreSQLContainer.getJdbcUrl());
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort()
                + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);

        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }

    public AuthorDto createAuthor(){
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
        var body = result.getResponseBody();
        assertNotNull(body);
        return body;
    }
    public BookDto createBookDto(UUID authorId){
        BookDto createBookDto = new BookDto(null, "book", "123.45.678", authorId);
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
        var body = result2.getResponseBody();
        assertNotNull(body);
        return body;
    }

    public EditionDto createEdition(){

         var author = createAuthor();
         var book  =createBookDto(author.getId());

        EditionDto edition = new EditionDto(
                book.id(),
                "9780123456789",
                300,
                LocalDate.of(2020, 5, 15),
                5,
                book.id()
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

        var body = editionResponseBody.getResponseBody();
        assertNotNull(body);
        return body;

    }

    public StorageDto createStorage(){

         var edition= createEdition();

         StorageDto storageDto = new StorageDto(null, 1, LocalDate.now(), BookState.FREE.getValue(), edition.id());
        var result2 =   webTestClient
                .post()
                .uri(STORAGE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(storageDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(StorageDto.class)
                .returnResult();
        var body = result2.getResponseBody();
        assertNotNull(body);
        return body;
    }




}
