package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.application.entity.StorageEntity;
import lombok.AllArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

@Component
@AllArgsConstructor
public class FetchQueries {

    private final DatabaseClient databaseClient;



    public Flux<StorageEntity> fetchStorages(String sql, SearchStorageDto searchStorageDto, int size, int offset) {
        return databaseClient.sql(sql)
                .bind("mode", !searchStorageDto.mode().equals(BookState.FREE))
                .bind("rack", searchStorageDto.rack())
                .bind("date_to", searchStorageDto.dateTo())
                .bind("date_from", searchStorageDto.dateFrom())
                .bind("size", size)
                .bind("offset", offset)
                .fetch()
                .all()
                .map((row) -> new StorageEntity(
                        (UUID) row.get("id"),
                        (Integer) row.get("rack"),
                        (LocalDate) row.get("accounting"),
                        (Boolean) row.get("is_taken"),
                        (UUID) row.get("book_edition_id")
                        )
                );
    }


    public Flux<AuthorEntity> fetchAuthors(String sql, SearchAuthorDto searchAuthorDto, int size, int offset) {

        return databaseClient.sql(sql)
                .bind("name", searchAuthorDto.name())
                .bind("size", size)
                .bind("offset", offset)
                .fetch()
                .all()
                .map((row) -> new AuthorEntity(
                                (UUID) row.get("id"),
                                (String) row.get("name")
                        )

                        );

    }

    public Flux<BookEntity> fetchBooks(String sql, SearchBookDto searchBookDto, int size, int offset) {
        return databaseClient.sql(sql)
                .bind("name", searchBookDto.name())
                .bind("udk", searchBookDto.udk())
                .bind("author", searchBookDto.author())
                .bind("size", size)

                .bind("offset", offset)
                .fetch()
                .all()
                .map((row) -> new BookEntity(
                                (UUID) row.get("id"),
                                (String) row.get("name"),
                        (String) row.get("udk"),
                        (UUID) row.get("author_id")
                        )

                );


    }



    public Flux<EditionEntity> fetchEdition(String sql, SearchEditionDto searchEditionDto, int size, int offset) {
        return databaseClient.sql(sql)
                .bind("isbn", searchEditionDto.isbn())
                .bind("number", searchEditionDto.number())
                .bind("publication", searchEditionDto.publication())
                .bind("name", searchEditionDto.name())
                .bind("size", size)
                .bind("offset", offset)
                .fetch()
                .all()
                .map((row) -> new EditionEntity(
                                (UUID) row.get("id"),
                                (String) row.get("isbn"),
                                (Integer) row.get("pages"),
                        (LocalDate) row.get("publication"),
                        (short) row.get("number"),
                        (UUID) row.get("book_id")
                        )

                );

    }
}
