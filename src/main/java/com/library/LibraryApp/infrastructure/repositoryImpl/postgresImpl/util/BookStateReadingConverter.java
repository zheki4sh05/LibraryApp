package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util;

import com.library.LibraryApp.application.dto.BookState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
@Slf4j
public class BookStateReadingConverter implements Converter<String, BookState> {
    @Override
    public BookState convert(String source) {
        log.info(source);
        return BookState.valueOf(source.toUpperCase());
    }
}

