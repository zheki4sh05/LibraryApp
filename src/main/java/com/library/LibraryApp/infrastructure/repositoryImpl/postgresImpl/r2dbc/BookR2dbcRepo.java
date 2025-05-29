package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc;


import com.library.LibraryApp.application.entity.BookEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.*;

@Repository
public interface BookR2dbcRepo extends R2dbcRepository<BookEntity, UUID> {

}

