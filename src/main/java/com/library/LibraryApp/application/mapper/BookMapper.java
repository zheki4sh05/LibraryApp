package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.BookDto;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.core.model.BookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(BookModel book);

    @Mapping(target = "id", ignore = true)
    BookModel toNewModel(BookDto bookDto);

    BookModel toModel(BookDto book);
    BookModel toModel(BookEntity book);
    BookEntity toEntity(BookModel newBook);

}
