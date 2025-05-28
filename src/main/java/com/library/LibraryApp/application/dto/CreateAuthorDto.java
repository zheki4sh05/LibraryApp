package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateAuthorDto {

    @NotBlank(
            message = "Поле с именем автора не может быть пустым")
    @Size(min = 4, max = 100)
    private String name;
}
