package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.BookDto;
import com.library.LibraryApp.application.dto.CreateBookDto;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.core.model.BookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(BookModel book);

    @Mapping(target = "id", ignore = true)
    BookModel toNewModel(CreateBookDto bookDto);

    @Mapping(target = "id", source = "id")
    BookModel toModel(BookDto book, UUID id);
    BookModel toModel(BookEntity book);
    BookEntity toEntity(BookModel newBook);

}
