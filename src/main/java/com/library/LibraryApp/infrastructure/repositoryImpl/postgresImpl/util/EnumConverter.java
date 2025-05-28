package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.library.LibraryApp.application.dto.BookState;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;
import org.springframework.stereotype.Component;

@ReadingConverter
@WritingConverter
public class EnumConverter implements Converter<BookState, BookState> {
    @Override
    public BookState convert(BookState source){
        return source;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return null;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return null;
    }
}

//public class EnumConverter extends EnumWriteSupport<BookState>{}

