package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;


import com.library.LibraryApp.application.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.r2dbc.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;

import java.util.*;

@Repository
public interface BookR2dbcRepo extends R2dbcRepository<BookEntity, UUID> {

    @Query("""
            select count(b.id) from book as b where b.author_id = :id
            """)
    Mono<ProjectionCount> countByAuthor(@Param("id") UUID id);

    Flux<BookEntity> findByNameContainingAndUdkContainingAndAuthorIn(String name, String udk, Collection<UUID> authorIds, Pageable pageable);

    Flux<ProjectionId> findAllByName(String name);
}


