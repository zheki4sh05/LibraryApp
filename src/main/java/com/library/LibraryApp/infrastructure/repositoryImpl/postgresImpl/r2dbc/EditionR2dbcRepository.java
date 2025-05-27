package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;

import com.library.LibraryApp.application.entity.EditionEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EditionR2dbcRepository extends R2dbcRepository<EditionEntity, UUID> {
}
