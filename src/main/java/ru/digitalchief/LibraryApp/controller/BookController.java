package ru.digitalchief.LibraryApp.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.digitalchief.LibraryApp.dto.*;
import ru.digitalchief.LibraryApp.exceptions.*;
import ru.digitalchief.LibraryApp.service.*;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestParam(name = "rack") Integer rack, @RequestParam(name = "isbn") Long isbn) {
        try {
             bookService.save(rack,isbn);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

    }
    @CrossOrigin
    @PutMapping("/borrow")
    public ResponseEntity<?> borrow(@RequestParam Long isbn) {
        try {
            bookService.borrow(isbn);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (NoFreeBooksException e) {
            return new ResponseEntity<>("", HttpStatus.CONFLICT);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BookDto book) {
        try {
            bookService.update(book);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer number) {
        try {
            bookService.delete(number);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

    }

}
