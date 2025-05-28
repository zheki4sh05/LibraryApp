package com.library.LibraryApp.application.dto;

import com.library.LibraryApp.web.markers.AdvancedInfo;
import com.library.LibraryApp.web.markers.BasicInfo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateStorageDto {


    @Min(value = 1 )
    @Max(value = 100)
    private Integer rack;

    @PastOrPresent
    private LocalDate accounting;

    @NotNull
    private BookState status;

    @NotNull
    private UUID edition;
}
