package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchEditionDto{

   @PastOrPresent
    private LocalDate publicationFrom=LocalDate.now();

    @PastOrPresent
    private LocalDate publicationTo=LocalDate.now();

    @Min(1)@Max(100)
    private Integer minNumber=1;

    @Min(1) @Max(100)
    private Integer maxNumber=100;

    @Size(max = 100)
    private  String name="";

    @Min(0) @Max(2000)
    private Integer pagesMin = 1;

    @Min(0) @Max(2000)
    private Integer pagesMax = 2000;

    @NotNull
    private UUID book;
}
