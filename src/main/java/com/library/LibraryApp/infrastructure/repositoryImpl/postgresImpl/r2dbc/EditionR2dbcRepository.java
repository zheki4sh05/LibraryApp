package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;

import com.library.LibraryApp.application.entity.EditionEntity;
import com.library.LibraryApp.core.model.*;
import org.springframework.data.domain.*;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.*;

import java.time.*;
import java.util.*;

@Repository
public interface EditionR2dbcRepository extends R2dbcRepository<EditionEntity, UUID> {

    Flux<EditionEntity> findByPagesBetweenAndPublicationBetweenAndNumberBetweenAndBook(
            int minPages,
            int maxPages,
            LocalDate startDate,
            LocalDate endDate,
            int minNumber,
            int maxNumber,
            UUID book,
            Pageable pageable
    );

    Mono<EditionModel> findByIsbn(String isbn);
}
