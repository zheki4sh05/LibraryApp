package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.application.entity.StorageEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class FetchQueries {

    private final DatabaseClient databaseClient;

    public Flux<StorageEntity> fetchStorages(SearchStorageDto searchStorageDto, Pageable pageable) {
        var sql = SqlQueryFactoryUtil.createStorageQuery();
        log.info(searchStorageDto.toString());
        return databaseClient.sql(sql)
                .bind("status", searchStorageDto.getStatus())
                .bind("rack", searchStorageDto.getRack())
                .bind("date_to", searchStorageDto.getDateTo())
                .bind("date_from", searchStorageDto.getDateFrom())
                .bind("size", pageable.getPageSize())
                .bind("offset", getOffset(pageable))
                .fetch()
                .all()
                .map((row) -> {
                           return new StorageEntity(
                                    (UUID) row.get("id"),
                                    (Integer) row.get("rack"),
                                    (LocalDate) row.get("accounting"),
                                    BookState.valueOf((String)row.get("status")),
                                    (UUID) row.get("book_edition_id")
                            );
                        }
                );
    }


    public Flux<AuthorEntity> fetchAuthors(SearchAuthorDto searchAuthorDto, Pageable pageable) {
       var sql = SqlQueryFactoryUtil.createAuthorQuery();
        return databaseClient.sql(sql)
                .bind("name", searchAuthorDto.name())
                .bind("size", pageable.getPageSize())
                .bind("offset", getOffset(pageable))
                .fetch()
                .all()
                .map((row) -> new AuthorEntity(
                                (UUID) row.get("id"),
                                (String) row.get("name")
                        )

                        );

    }

    public Flux<BookEntity> fetchBooks(SearchBookDto searchBookDto, Pageable pageable) {
        var sql = SqlQueryFactoryUtil.createBookQuery(pageable);
        return databaseClient.sql(sql)
                .bind("name", searchBookDto.getName())
                .bind("udk", searchBookDto.getUdk())
                .bind("author", searchBookDto.getAuthor())
                .bind("size", pageable.getPageSize())
                .bind("offset",  getOffset(pageable))
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


    public Flux<EditionEntity> fetchEdition(SearchEditionDto searchEditionDto, Pageable pageable) {

        var sql = SqlQueryFactoryUtil.createEditionQuery();

        return databaseClient.sql(sql)
                .bind("isbn", searchEditionDto.getIsbn())
                .bind("number", searchEditionDto.getNumber())
                .bind("publication", searchEditionDto.getPublication())
                .bind("name", searchEditionDto.getName())
                .bind("size", pageable.getPageSize())
                .bind("offset", getOffset(pageable))
                .fetch()
                .all()
                .map((row) -> new EditionEntity(
                                (UUID) row.get("id"),
                                (String) row.get("isbn"),
                                (Integer) row.get("pages"),
                        (LocalDate) row.get("publication"),
                        (Integer) row.get("number"),
                        (UUID) row.get("book_id")
                        )

                );

    }

    private int getOffset(Pageable pageable){
        return SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), pageable.getPageSize());
    }


}
