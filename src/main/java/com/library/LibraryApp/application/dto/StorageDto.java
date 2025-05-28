package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.*;
import lombok.ToString;

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
