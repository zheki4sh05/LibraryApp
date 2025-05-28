package com.library.LibraryApp.application.dto;


import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {

    @NotNull(groups = AdvancedInfo.class)
    private UUID id;

    @NotBlank(message = "Поле с именем автора не может быть пустым", groups = {BasicInfo.class, AdvancedInfo.class})
    @Size(min = 4, max = 100,groups = {BasicInfo.class, AdvancedInfo.class})
    private String name;
}
