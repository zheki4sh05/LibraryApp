package com.library.LibraryApp.web.dto;


import com.library.LibraryApp.core.util.RegexPatterns;
import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {
    @Pattern(regexp = RegexPatterns.UUID, groups = AdvancedInfo.class)
    @NotBlank(groups = AdvancedInfo.class)
    private String id;

    @NotBlank(message = "Поле с именем автора не может быть пустым", groups = {BasicInfo.class, AdvancedInfo.class})
    @Size(min = 4, max = 100,groups = {BasicInfo.class, AdvancedInfo.class})
    private String name;
}
