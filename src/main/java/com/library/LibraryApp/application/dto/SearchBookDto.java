package com.library.LibraryApp.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchBookDto
 {

    private  String udk="";

     private String name="";

     private  String author="";
}
