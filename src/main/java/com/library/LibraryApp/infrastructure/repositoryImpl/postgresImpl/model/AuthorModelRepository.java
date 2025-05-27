package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.SearchAuthorDto;
import com.library.LibraryApp.application.mapper.AuthorMapper;
import com.library.LibraryApp.core.model.AuthorModel;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.AuthorR2dbcRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AuthorModelRepository implements AuthorRepository {

    private final AuthorR2dbcRepo authorR2dbcRepo;
    private final AuthorMapper authorMapper;

    @Override
    public Mono<AuthorModel> save(AuthorModel newAuthor) {
        return authorR2dbcRepo.save(authorMapper.fromModel(newAuthor)).map(authorMapper::toModel);
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<AuthorModel> findById(UUID id) {
        return authorR2dbcRepo.findById(id).map(authorMapper::toModel);
    }

    @Override
    public Mono<Page<AuthorModel>> fetchAuthors(SearchAuthorDto searchAuthorDto, Pageable pageable) {
        return null;
    }

    @Override
    public Mono<Void> delete(AuthorModel author) {
        return authorR2dbcRepo.delete(authorMapper.fromModel(author));
    }
}
