package com.library.LibraryApp.application.dto;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    private UUID id;
    private String name;
}
