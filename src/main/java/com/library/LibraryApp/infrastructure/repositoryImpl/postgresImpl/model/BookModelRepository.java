package com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.model;

import com.library.LibraryApp.application.dto.SearchBookDto;
import com.library.LibraryApp.application.mapper.BookMapper;
import com.library.LibraryApp.core.model.BookModel;
import com.library.LibraryApp.core.repository.BookRepository;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.r2dbc.BookR2dbcRepo;
import com.library.LibraryApp.infrastructure.repositoryImpl.postgresImpl.util.FetchQueries;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BookModelRepository implements BookRepository {

    private final BookR2dbcRepo bookR2dbcRepo;
    private final BookMapper bookMapper;
    private final FetchQueries fetchQueries;

    @Override
    public Mono<BookModel> save(BookModel newBook) {
        return bookR2dbcRepo.save(bookMapper.toEntity(newBook)).map(bookMapper::toModel);
    }

    @Override
    public Mono<Page<BookModel>> fetchBooks(SearchBookDto searchBookDto, Pageable pageable) {
        return fetchQueries.fetchBooks(searchBookDto, pageable)
                .map(bookMapper::toModel)
                .collectList()
                .flatMap(bookModels -> bookR2dbcRepo.count()
                        .map(total->new PageImpl<>(bookModels, pageable, total))
                );
    }

    @Override
    public Mono<Void> delete(BookModel book) {
        return bookR2dbcRepo.delete(bookMapper.toEntity(book));
    }

    @Override
    public Mono<BookModel> findById(UUID id) {
        return bookR2dbcRepo.findById(id).map(bookMapper::toModel);
    }

    @Override
    public Mono<Long> countByAuthor(UUID id) {
        return bookR2dbcRepo.countByAuthor(id).count();
    }
}
