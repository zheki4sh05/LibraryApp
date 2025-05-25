package com.library.LibraryApp.web.controller;

import com.library.LibraryApp.core.service.EditionService;
import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.dto.EditionDto;
import com.library.LibraryApp.web.dto.SearchEditionDto;
import com.library.LibraryApp.web.mapper.EditionMapper;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

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
     return editionService.create(editionMapper.toNewEntity(editionDto)).map(editionMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<EditionDto> getEditionById(
            @PathVariable @NotBlank @Pattern(regexp = RegexPatterns.UUID) String id) {
        return editionService.findById(id).map(editionMapper::toDto);
    }

    @GetMapping("/fetch")
    public Mono<Page<EditionDto>> fetch(
        @RequestParam(value = "isbn" ,defaultValue = "", required = false) String isbn,
        @RequestParam(value = "publication" ,defaultValue = "1970-01-01", required = false) @Past @DateTimeFormat(pattern = "yyyy-mm-dd")  LocalDate publication,
        @RequestParam(value = "number" ,defaultValue = "1", required = false) @Min(1) Integer number,
        @RequestParam(value = "name" ,defaultValue = "", required = false) @Size(max = 100) String name,
        Pageable pageable
    ){
        SearchEditionDto searchEditionDto = new SearchEditionDto(isbn, publication, number,name);
        return editionService.fetch(searchEditionDto, pageable).map(page -> page.map(editionMapper::toDto));
    };

    @PutMapping
    public Mono<EditionDto> updateEdition(@RequestBody
                                              @Validated(AdvancedInfo.class) EditionDto editionDto) {
        return editionService.update(editionMapper.toEntity(editionDto)).map(editionMapper::toDto);
    }

    @DeleteMapping("/{id}")
    public Mono<String> deleteEdition(
            @NotBlank  @PathVariable String id) {
        return editionService.delete(id);
    }
}

