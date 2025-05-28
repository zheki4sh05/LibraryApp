package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.*;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

public record StorageDto(


    @NotNull(groups = {AdvancedInfo.class})
    UUID id,

    @Min(value = 1, groups = {AdvancedInfo.class, BasicInfo.class} )
    @Max(value = 100, groups = {AdvancedInfo.class, BasicInfo.class})
    Integer rack,

   @PastOrPresent(groups = {AdvancedInfo.class, BasicInfo.class})
    LocalDate accounting,

    @NotNull(groups = {AdvancedInfo.class, BasicInfo.class})
    BookState status,

    @NotNull(groups = {AdvancedInfo.class, BasicInfo.class})
    UUID edition
) {
}
