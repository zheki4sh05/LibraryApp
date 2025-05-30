package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;

import com.library.LibraryApp.application.entity.AuthorEntity;
import com.library.LibraryApp.core.model.AuthorModel;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface AuthorR2dbcRepo extends ReactiveCrudRepository<AuthorEntity, UUID>, R2dbcRepository<AuthorEntity, UUID> {

//    @Query("""
//            select a.* from author as a
//            where (:name = '' or a.name like :name)
//            """)
    Flux<AuthorEntity> findByNameContainingOrIsEmpty(@Param("name") String name, Pageable pageable);
}
