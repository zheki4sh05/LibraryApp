package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.dto.CreateStorageDto;
import com.library.LibraryApp.application.dto.SearchStorageDto;
import com.library.LibraryApp.application.dto.StorageDto;
import com.library.LibraryApp.application.mapper.StorageMapper;
import com.library.LibraryApp.core.service.StorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/storage")
@AllArgsConstructor
public class StorageController {

    private final StorageMapper storageMapper;
    private final StorageService storageService;

    @PostMapping
    public Mono<StorageDto> createStorage(
           @Valid @RequestBody CreateStorageDto storageDto
    ){
        return storageService.create(storageMapper.toNewModel(storageDto)).map(storageMapper::toDto);

    }

    @GetMapping
    public Mono<Page<StorageDto>> fetch(
         @Valid   SearchStorageDto searchStorageDto,
            Pageable pageable
            ){

        return storageService.fetch(searchStorageDto,pageable).map(page -> page.map(storageMapper::toDto));

    }

    @GetMapping("/find")
    public Mono<Page<StorageDto>> findAllByEdition(
            @RequestParam(value = "edition") @NotNull UUID editionId,
            Pageable pageable
    ){
        return storageService.findAllByEdition(editionId,pageable).map(page -> page.map(storageMapper::toDto));

    }

    @DeleteMapping("/{id}")
    public Mono<UUID> deleteById(
            @PathVariable("id") @NotNull UUID id
    ){
        return storageService.deleteById(id);

    }

    @PutMapping("/{id}")
    public Mono<StorageDto>  updateStorage(
            @PathVariable UUID id,
          @Valid  @RequestBody CreateStorageDto storageDto
    ){
        return storageService.update(storageMapper.toModel(storageDto,id)).map(storageMapper::toDto);

    }
}
