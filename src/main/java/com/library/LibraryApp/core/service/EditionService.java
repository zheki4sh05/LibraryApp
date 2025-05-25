package com.library.LibraryApp.core.service;

import com.library.LibraryApp.core.entity.Edition;
import com.library.LibraryApp.web.dto.SearchEditionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

public interface EditionService {
    Mono<Edition> create(Edition edition);

    Mono<Edition> findById(String id);

    Mono<Edition> update(Edition entity);

    Mono<String> delete(String id);

    Mono<Page<Edition>> fetch(SearchEditionDto searchEditionDto, Pageable pageable);
}
