package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.application.mapper.StorageMapper;
import com.library.LibraryApp.core.service.StorageService;
import com.library.LibraryApp.application.dto.SearchStorageDto;
import com.library.LibraryApp.application.dto.StorageDto;
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
@RequestMapping("/storage")
@AllArgsConstructor
@Slf4j
public class StorageController {

    private final StorageMapper storageMapper;
    private final StorageService storageService;

    @PostMapping
    public Mono<StorageDto> createStorage(
            @RequestBody
            @Validated(BasicInfo.class) StorageDto storageDto
    ){
        return storageService.create(storageMapper.toNewModel(storageDto)).map(storageMapper::toDto);

    }

    @GetMapping
    public Mono<Page<StorageDto>> fetch(
         @ModelAttribute @Valid SearchStorageDto searchStorageDto,
            Pageable pageable
            ){
        var result = storageService.fetch(searchStorageDto,pageable).map(page -> page.map(storageMapper::toDto));
        log.info(searchStorageDto.toString());
        return result;

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

    @PatchMapping
    public Mono<StorageDto>  updateStorage(
            @RequestBody
            @Validated(AdvancedInfo.class) StorageDto storageDto
    ){
        return storageService.update(storageMapper.toModel(storageDto)).map(storageMapper::toDto);

    }






}
