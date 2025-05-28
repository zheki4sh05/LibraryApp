package com.library.LibraryApp.core.model;

import com.library.LibraryApp.application.dto.BookState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageModel {

    private UUID id;
    private Integer rack;
    private LocalDate accounting;
    private BookState status;
    private UUID edition;

}
