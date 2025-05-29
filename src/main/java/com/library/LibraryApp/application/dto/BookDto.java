package com.library.LibraryApp.application.dto;


import lombok.*;

import java.util.UUID;


@Builder
public record BookDto(


        UUID id,


        String name,


            String udk,


        UUID author

){



}
