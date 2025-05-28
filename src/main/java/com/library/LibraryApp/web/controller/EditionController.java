package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.mapper.EditionMapper;
import com.library.LibraryApp.core.service.EditionService;
import com.library.LibraryApp.application.dto.EditionDto;
import com.library.LibraryApp.application.dto.SearchEditionDto;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/edition")
@AllArgsConstructor
@Slf4j
public class EditionController {

    private final EditionService editionService;
    private final EditionMapper editionMapper;

    @PostMapping
    public Mono<EditionDto> createEdition(
            @RequestBody
            @Validated(BasicInfo.class) EditionDto editionDto
    ) {

     return editionService.create(editionMapper.toNewModel(editionDto)).map(editionMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<EditionDto> findById(
            @PathVariable @NotNull UUID id) {
        return editionService.findById(id).map(editionMapper::toDto);
    }

    @GetMapping("/fetch")
    public Mono<Page<EditionDto>> fetch(
           @Valid SearchEditionDto searchEditionDto,
        Pageable pageable
    ){
        log.info(searchEditionDto.toString());
        return editionService.fetch(searchEditionDto, pageable).map(page -> page.map(editionMapper::toDto));
    };

    @PutMapping
    public Mono<EditionDto> updateEdition(@RequestBody
                                              @Validated(AdvancedInfo.class) EditionDto editionDto) {
        return editionService.update(editionMapper.toModel(editionDto)).map(editionMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<UUID> deleteEdition(
            @NotNull  @PathVariable UUID id) {
        return editionService.delete(id);
    }
}

