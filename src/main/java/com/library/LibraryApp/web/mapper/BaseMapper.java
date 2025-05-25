package com.library.LibraryApp.web.mapper;

import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface BaseMapper {
    @Named("reformat_date")
    default String reformat(LocalDate inputDate){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(inputDate.toString(), inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return localDate.format(outputFormatter);
    }
}
