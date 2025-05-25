package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.core.service.StorageService;
import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.dto.BookState;
import com.library.LibraryApp.web.dto.SearchStorageDto;
import com.library.LibraryApp.web.dto.StorageDto;
import com.library.LibraryApp.web.mapper.StorageMapper;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/storage")
@AllArgsConstructor
public class StorageController {

    private final StorageMapper storageMapper;
    private final StorageService storageService;

    @PostMapping
    public Mono<StorageDto> createStorage(
            @RequestBody
            @Validated(BasicInfo.class) StorageDto storageDto
    ){
        return storageService.create(storageMapper.toNewEntity(storageDto)).map(storageMapper::toDto);

    }

    @GetMapping
    public Mono<Page<StorageDto>> fetch(
            @RequestParam(value = "mode") BookState bookState,
            @RequestParam(value = "from", required = false, defaultValue = "1970-01-01") LocalDate dateFrom,
            @RequestParam(value = "to", required = false, defaultValue = "") LocalDate dateTo,
            @RequestParam(value = "rack", required = false, defaultValue = "1") @Min(1) @Max(100) Integer rack,
            Pageable pageable
            ){
        if(dateTo==null){
            dateTo = LocalDate.now();
        }
        SearchStorageDto searchStorageDto = new SearchStorageDto
                ( bookState, dateFrom,dateTo, rack);
        return storageService.fetch(searchStorageDto,pageable).map(page -> page.map(storageMapper::toDto));

    }

    @GetMapping("/find")
    public Mono<Page<StorageDto>> findAllByEdition(
            @RequestParam(value = "edition") @Pattern(regexp = RegexPatterns.UUID) String editionId,
            Pageable pageable
    ){
        return storageService.findAllByEdition(editionId,pageable).map(page -> page.map(storageMapper::toDto));

    }

    @DeleteMapping("/{id}")
    public Mono<String> deleteById(
            @PathVariable("id") @Pattern(regexp = RegexPatterns.UUID) String id
    ){
        return storageService.deleteById(id);

    }

    @PatchMapping
    public Mono<StorageDto>  updateStorage(
            @RequestBody
            @Validated(AdvancedInfo.class) StorageDto storageDto
    ){
        return storageService.update(storageMapper.toEntity(storageDto)).map(storageMapper::toDto);

    }






}
