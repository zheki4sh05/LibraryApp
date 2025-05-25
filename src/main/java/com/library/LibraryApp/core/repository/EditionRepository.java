package com.library.LibraryApp.core.repository;

import com.library.LibraryApp.core.entity.Edition;
import com.library.LibraryApp.core.entity.Storage;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EditionRepository extends R2dbcRepository<Edition, UUID> {
}
