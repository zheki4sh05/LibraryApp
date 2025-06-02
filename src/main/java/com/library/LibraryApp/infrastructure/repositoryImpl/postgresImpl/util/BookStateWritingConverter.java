package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util;

import com.library.LibraryApp.application.dto.BookState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
@Slf4j
public class BookStateWritingConverter implements Converter<BookState, BookState> {
    @Override
    public BookState convert(BookState source) {
        return source;
    }
}

