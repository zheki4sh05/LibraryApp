package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;


@Builder
public record BookDto(

        @NotNull(groups = {AdvancedInfo.class})
        UUID id,

        @NotBlank(groups = {AdvancedInfo.class, BasicInfo.class})
        @Size(min = 1, max = 100, groups = {AdvancedInfo.class, BasicInfo.class})
        String name,

        @Pattern(regexp = RegexPatterns.UDK, message = "Некорректный формат УДК", groups = {AdvancedInfo.class, BasicInfo.class})
        @Size(min = 3, max = 20, message = "УДК должен содержать от 3 до 20 символов", groups = {AdvancedInfo.class, BasicInfo.class})
            String udk,

        @NotNull(groups = {AdvancedInfo.class, BasicInfo.class})
        UUID author

){



}
