package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.core.util.SqlQueryFactoryUtil;
import com.library.LibraryApp.core.entity.Edition;
import com.library.LibraryApp.core.repository.BookRepository;
import com.library.LibraryApp.core.repository.EditionRepository;
import com.library.LibraryApp.core.repository.FetchQueries;
import com.library.LibraryApp.core.service.EditionService;
import com.library.LibraryApp.web.dto.SearchEditionDto;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BookEditionService implements EditionService {

    private final EditionRepository editionRepository;
    private final BookRepository bookRepository;
    private final FetchQueries fetchQueries;

    @Override
    public Mono<Edition> create(Edition edition) {

        return bookRepository.findById(edition.getBookId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id "+edition.getBookId())))
                .flatMap(book -> editionRepository.save(edition));
    }

    @Override
    public Mono<Edition> findById(String id) {
        return editionRepository.findById(UUID.fromString(id)).switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти издание по id")));
    }


    @Override
    public Mono<Edition> update(Edition entity) {

        return editionRepository.findById(entity.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id "+entity.getId())))
                .flatMap(findEntity->editionRepository.save(entity));
    }

    @Override
    public Mono<String> delete(String id) {
        return editionRepository.findById(UUID.fromString(id))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id "+id)))
                .flatMap(editionRepository::delete)
                .thenReturn(id);
    }

    @Override
    public Mono<Page<Edition>> fetch(SearchEditionDto searchEditionDto, Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = SqlQueryFactoryUtil.calcOffset(pageable.getPageNumber(), size);
        String sql = SqlQueryFactoryUtil.createEditionQuery();
        Flux<Edition> editionFlux = fetchQueries.fetchEdition(sql, searchEditionDto, size, offset);
        return editionFlux.collectList()
                .zipWith(editionRepository.count())
                .map(tuple->
                        new PageImpl<>(tuple.getT1(), pageable, tuple.getT2())
                );

    }
}
