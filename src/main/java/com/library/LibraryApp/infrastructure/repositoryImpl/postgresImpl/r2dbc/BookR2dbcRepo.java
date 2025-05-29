package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;


import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.application.entity.ProjectionId;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.*;

@Repository
public interface BookR2dbcRepo extends R2dbcRepository<BookEntity, UUID> {

    @Query("""
            select b.id from book as b
            join author a on a.id = b.author_id and a.id = :id
            """)
    Flux<ProjectionId> countByAuthor(@Param("id") UUID id);
}

