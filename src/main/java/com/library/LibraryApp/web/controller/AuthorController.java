package com.library.LibraryApp.web.controller;


import com.library.LibraryApp.core.service.AuthorService;
import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.dto.AuthorDto;
import com.library.LibraryApp.web.dto.SearchAuthorDto;
import com.library.LibraryApp.web.mapper.AuthorMapper;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/author")
@AllArgsConstructor
public class AuthorController {

    private final AuthorMapper authorMapper;
    private final AuthorService authorService;

    @PostMapping
    Mono<AuthorDto> createAuthor(
            @RequestBody @Validated(BasicInfo.class) AuthorDto createAuthorDto){
        return authorService.create(authorMapper.toNewEntity(createAuthorDto)).map(authorMapper::toDto);
    }

    @GetMapping
    Mono<Page<AuthorDto>> fetch(
            @RequestParam(name = "name",required = false, defaultValue = "")
            String name,
            Pageable pageable
    ){
        SearchAuthorDto searchAuthorDto = new SearchAuthorDto(name);
        return authorService.fetch(searchAuthorDto,pageable).map(page -> page.map(authorMapper::toDto));

    }

    @GetMapping("/{id}")
    Mono<AuthorDto> findById(
            @PathVariable("id") @NotBlank @Pattern(regexp = RegexPatterns.UUID) String id
    ){
        return authorService.findById(id).map(authorMapper::toDto);
    }

    @PatchMapping
    Mono<AuthorDto> update(
            @RequestBody
            @Validated(AdvancedInfo.class) AuthorDto authorDto){
        return authorService.update(authorMapper.toEntity(authorDto)).map(authorMapper::toDto);
    }

    @DeleteMapping("/{id}")
    Mono<String> deleteById(
            @PathVariable("id") @NotBlank @Pattern(regexp = RegexPatterns.UUID) String id){
        return authorService.deleteById(id);
    }



}
