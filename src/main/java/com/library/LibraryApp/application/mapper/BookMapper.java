package com.library.LibraryApp.application.mapper;

import com.library.LibraryApp.application.dto.BookDto;
import com.library.LibraryApp.application.entity.BookEntity;
import com.library.LibraryApp.core.model.BookModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mappings({
            @Mapping(target = "id", expression = "java(book.getId().toString())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "udk", source = "udk"),
            @Mapping(target = "author", expression = "java(book.getAuthor().toString())"),
    })
    BookDto toDto(BookModel book);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(book.id()))")
    @Mapping(target = "author", expression = "java(java.util.UUID.fromString(book.author()))")
    BookEntity toEntity(BookDto book);


    @Mapping(target = "id", ignore = true)
    BookModel toNewModel(BookDto bookDto);

    BookModel toModel(BookDto book);
    BookModel toModel(BookEntity book);
    BookEntity toEntity(BookModel newBook);

}
