package com.library.LibraryApp.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record StorageDto(



    UUID id,


    Integer rack,


    LocalDate accounting,


    String status,


    UUID edition
) {
}
