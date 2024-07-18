package ru.digitalchief.LibraryApp.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.digitalchief.LibraryApp.Repository.*;
import ru.digitalchief.LibraryApp.dto.*;
import ru.digitalchief.LibraryApp.entity.*;
import ru.digitalchief.LibraryApp.exceptions.*;

import java.time.*;

@Service
public class BookService {


    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private BookRepository bookRepository;


    public void save(Integer rack, Long isbn) throws BadRequestException {

        Book book = new Book();

        Catalog catalog = catalogRepository.getByUsbn(isbn).orElseThrow(BadRequestException::new);

        book.setCatalogFk(catalog);
        book.setRack(rack);
        book.setAccounting(LocalDate.now());
        book.setIsTaken(false);


        bookRepository.save(book);

    }

    public void borrow(Long isbn) throws NoFreeBooksException, BadRequestException{

        Book book = bookRepository.findFirstWithFreeBook(isbn).orElseThrow(NoFreeBooksException::new);

        book.setIsTaken(true);

        bookRepository.save(book);
    }

    public void update(BookDto book) throws BadRequestException{

        Catalog catalog = catalogRepository.getByUsbn(book.getIsbn()).orElseThrow(BadRequestException::new);

        Book updatedBook = mapToEntity(book);

        updatedBook.setCatalogFk(catalog);

        bookRepository.save(updatedBook);

    }

    private Book mapToEntity(BookDto bookDto){
        return new Book(bookDto.getId(),
                bookDto.getRack(),
                bookDto.getAccounting(),
                bookDto.getIsTaken()
        );
    }


    public void delete(Integer number)throws BadRequestException {

        Book book = bookRepository.findByNumber(number);

        bookRepository.delete(book);

    }
}
