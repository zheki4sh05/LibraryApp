package com.library.LibraryApp.core.repository;

import com.library.LibraryApp.core.entity.Author;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorRepository extends R2dbcRepository<Author, UUID> {
}
