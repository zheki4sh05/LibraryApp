package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.SearchEditionDto;
import com.library.LibraryApp.application.mapper.EditionMapper;
import com.library.LibraryApp.core.model.EditionModel;
import com.library.LibraryApp.core.repository.EditionRepository;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.EditionR2dbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@AllArgsConstructor
public class EditionModelRepository implements EditionRepository {

    private final EditionR2dbcRepository editionR2dbcRepository;
    private final EditionMapper editionMapper;

    @Override
    public Mono<EditionModel> save(EditionModel edition) {
        return editionR2dbcRepository.save(editionMapper.toEntity(edition)).map(editionMapper::toModel);
    }

    @Override
    public Mono<EditionModel> findById(UUID id) {
        return editionR2dbcRepository.findById(id).map(editionMapper::toModel);
    }

    @Override
    public Mono<Void> delete(EditionModel editionModel) {
        return editionR2dbcRepository.delete(editionMapper.toEntity(editionModel));
    }

    @Override
    public Mono<Long> count() {
        return editionR2dbcRepository.count();
    }

    @Override
    public Mono<Page<EditionModel>> fetchEditions(SearchEditionDto searchEditionDto, Pageable pageable) {
        return null;
    }
}
