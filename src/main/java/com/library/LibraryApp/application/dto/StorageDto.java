package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record StorageDto(

    @Pattern(regexp = RegexPatterns.UUID, groups = {AdvancedInfo.class})
    @NotBlank(groups = {AdvancedInfo.class})
    String id,

    @Min(value = 1, groups = {AdvancedInfo.class, BasicInfo.class} )
    @Max(value = 100, groups = {AdvancedInfo.class, BasicInfo.class})
    Integer rack,

   @PastOrPresent(groups = {AdvancedInfo.class, BasicInfo.class})
    LocalDate accounting,

    @NotNull(groups = {AdvancedInfo.class, BasicInfo.class})
    Boolean taken,

    @NotBlank(groups = {AdvancedInfo.class, BasicInfo.class})
    @Pattern(regexp = RegexPatterns.UUID, groups = {AdvancedInfo.class, BasicInfo.class})
    String edition
) {
}
