package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.*;
import com.library.LibraryApp.application.mapper.*;
import com.library.LibraryApp.core.model.*;
import com.library.LibraryApp.core.repository.*;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.*;
import lombok.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;

import java.util.*;

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
      return  editionR2dbcRepository.findByPagesBetweenAndPublicationBetweenAndNumberBetweenAndBook(
                        searchEditionDto.getPagesMin(),
                       searchEditionDto.getPagesMax(),
                        searchEditionDto.getPublicationFrom(),
                        searchEditionDto.getPublicationTo(),
                        searchEditionDto.getMinNumber(),
                        searchEditionDto.getMaxNumber(),
                        searchEditionDto.getBook(),
                        pageable
                )
                .map(editionMapper::toModel)
                .collectList()
                .flatMap(authorModels -> editionR2dbcRepository.count()
                        .map(total->new PageImpl<>(authorModels, pageable, total))
                );
    }

    @Override
    public Mono<EditionModel> findByIsbn(String isbn) {
        return editionR2dbcRepository.findByIsbn(isbn);
    }

}
