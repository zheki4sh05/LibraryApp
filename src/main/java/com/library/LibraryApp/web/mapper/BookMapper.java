package com.library.LibraryApp.web.mapper;

import com.library.LibraryApp.core.entity.Book;
import com.library.LibraryApp.web.dto.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper{
    @Mappings({
            @Mapping(target = "id", expression = "java(book.getId().toString())"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "udk", source = "udk"),
            @Mapping(target = "author", expression = "java(book.getAuthor().toString())"),
    })
    BookDto toDto(Book book);


    @Mappings({
            @Mapping(target = "id", expression = "java(java.util.UUID.fromString(book.id()))"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "udk", source = "udk"),
            @Mapping(target = "author", expression = "java(java.util.UUID.fromString(book.author()))"),
    })
    Book toEntity(BookDto book);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "udk", source = "udk"),
            @Mapping(target = "author", expression = "java(java.util.UUID.fromString(bookDto.author()))"),
    })
    Book toNewEntity(BookDto bookDto);
}
