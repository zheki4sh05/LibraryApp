package com.library.LibraryApp.core.service.impl;


import com.library.LibraryApp.application.dto.SearchAuthorDto;
import com.library.LibraryApp.core.model.AuthorModel;
import com.library.LibraryApp.core.repository.AuthorRepository;
import com.library.LibraryApp.core.repository.BookRepository;
import com.library.LibraryApp.core.service.AuthorService;
import com.library.LibraryApp.web.exceptions.ChildConstraintException;
import com.library.LibraryApp.web.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BookAuthorService implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public Mono<AuthorModel> create(AuthorModel newAuthor) {
        return authorRepository.save(newAuthor);
    }

    @Override
    public Mono<Page<AuthorModel>> fetch(SearchAuthorDto searchAuthorDto, Pageable pageable) {
        return authorRepository.fetchAuthors(searchAuthorDto, pageable);
    }


    @Override
    public Mono<AuthorModel> findById(UUID id) {

        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти автора по id " +id)));
    }

    @Override
    public Mono<AuthorModel> update(AuthorModel updatedAuthor) {
        return authorRepository.save(updatedAuthor);
    }

    @Override
    public Mono<UUID> deleteById(UUID id) {
        return authorRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Не удалось найти для удаления автора по id "+id)))
                .flatMap(authorModel ->  bookRepository.countByAuthor(authorModel.getId())
                        .flatMap(count->{
                            if(count>0){
                                throw new ChildConstraintException("У автора с id "+id+" есть связанные сущности: "+count);
                            }else{
                                return authorRepository.delete(authorModel);
                            }
                        })
                )
                .thenReturn(id);
    }
}
