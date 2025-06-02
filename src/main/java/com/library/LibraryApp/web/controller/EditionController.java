package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.dto.CreateEditionDto;
import com.library.LibraryApp.application.dto.EditionDto;
import com.library.LibraryApp.application.dto.SearchEditionDto;
import com.library.LibraryApp.application.mapper.EditionMapper;
import com.library.LibraryApp.core.service.EditionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/edition")
@AllArgsConstructor
public class EditionController {

    private final EditionService editionService;
    private final EditionMapper editionMapper;

    @PostMapping
    public Mono<EditionDto> createEdition(
            @Valid @RequestBody
            CreateEditionDto editionDto
    ) {

     return editionService.create(editionMapper.toNewModel(editionDto)).map(editionMapper::toDto);
    }

    @GetMapping("/id/{id}")
    public Mono<EditionDto> findById(
            @PathVariable @NotNull UUID id) {
        return editionService.findById(id).map(editionMapper::toDto);
    }

    @GetMapping("/isbn/{isbn}")
    public Mono<EditionDto> findByIsbn(
            @PathVariable @NotNull String isbn) {
        return editionService.findByIsbn(isbn).map(editionMapper::toDto);
    }

    @GetMapping("/fetch")
    public Mono<Page<EditionDto>> fetch(
           @Valid SearchEditionDto searchEditionDto,
        Pageable pageable
    ){
        return editionService.fetch(searchEditionDto, pageable).map(page -> page.map(editionMapper::toDto));
    };

    @PutMapping("{id}")
    public Mono<EditionDto> updateEdition(
            @PathVariable UUID id,
            @Valid @RequestBody CreateEditionDto editionDto) {
        return editionService.update(editionMapper.toModel(editionDto,id)).map(editionMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<UUID> deleteEdition(
            @NotNull  @PathVariable UUID id) {
        return editionService.delete(id);
    }
}

