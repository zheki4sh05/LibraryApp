package com.library.LibraryApp.web.controller;


import com.library.LibraryApp.application.mapper.AuthorMapper;
import com.library.LibraryApp.core.service.AuthorService;
import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.application.dto.AuthorDto;
import com.library.LibraryApp.application.dto.SearchAuthorDto;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/author")
@AllArgsConstructor
public class AuthorController {

    private final AuthorMapper authorMapper;
    private final AuthorService authorService;

    @PostMapping
    Mono<AuthorDto> createAuthor(
            @RequestBody @Validated(BasicInfo.class) AuthorDto createAuthorDto){
        return authorService.create(authorMapper.toNewModel(createAuthorDto)).map(authorMapper::toDto);
    }

    @GetMapping
    Mono<Page<AuthorDto>> fetch(
       @ModelAttribute  @Valid SearchAuthorDto searchAuthorDto,
            Pageable pageable
    ){

        return authorService.fetch(searchAuthorDto,pageable).map(page -> page.map(authorMapper::toDto));

    }

    @GetMapping("/{id}")
    Mono<AuthorDto> findById(
            @PathVariable("id") @NotNull UUID id
    ){
        return authorService.findById(id).map(authorMapper::toDto);
    }

    @PatchMapping
    Mono<AuthorDto> update(
            @RequestBody
            @Validated(AdvancedInfo.class) AuthorDto authorDto){
        return authorService.update(authorMapper.toModel(authorDto)).map(authorMapper::toDto);
    }

    @DeleteMapping("/{id}")
    Mono<UUID> deleteById(
            @PathVariable("id") @NotNull UUID id){
        return authorService.deleteById(id);
    }



}
