package com.library.LibraryApp.core.service.impl;

import com.library.LibraryApp.application.dto.SearchEditionDto;
import com.library.LibraryApp.core.model.EditionModel;
import com.library.LibraryApp.core.repository.BookRepository;
import com.library.LibraryApp.core.repository.EditionRepository;
import com.library.LibraryApp.core.service.EditionService;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BookEditionService implements EditionService {

    private final EditionRepository editionRepository;
    private final BookRepository bookRepository;

    @Override
    public Mono<EditionModel> create(EditionModel edition) {
        return bookRepository.findById(edition.getBook())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id "+edition.getBook())))
                .flatMap(book -> editionRepository.save(edition));
    }

    @Override
    public Mono<EditionModel> findById(UUID id) {
        return editionRepository.findById(id).switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти издание по id")));
    }


    @Override
    public Mono<EditionModel> update(EditionModel entity) {

        return editionRepository.findById(entity.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id "+entity.getId())))
                .flatMap(findEntity->editionRepository.save(entity));
    }

    @Override
    public Mono<UUID> delete(UUID id) {
        return editionRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по id "+id)))
                .flatMap(editionRepository::delete)
                .thenReturn(id);
    }

    @Override
    public Mono<Page<EditionModel>> fetch(SearchEditionDto searchEditionDto, Pageable pageable) {
        log.info(searchEditionDto.toString());
        return editionRepository.fetchEditions(searchEditionDto, pageable);


    }

    @Override
    public Mono<EditionModel> findByIsbn(String isbn) {
        return editionRepository.findByIsbn(isbn)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти выпуск по isbn: "+isbn)));
    }
}
