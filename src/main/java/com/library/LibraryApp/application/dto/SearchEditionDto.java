package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchEditionDto{

       private  String isbn="";

       @PastOrPresent
        private LocalDate publication=LocalDate.now();
        @Min(1)
        private Integer number=1;
@Size(max = 100)
        private  String name="";
}
