package ru.digitalchief.LibraryApp.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.digitalchief.LibraryApp.dto.*;
import ru.digitalchief.LibraryApp.exceptions.*;
import ru.digitalchief.LibraryApp.service.*;

import java.util.*;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;
    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody CatalogDto catalogDto) {
        try {
            catalogService.save(catalogDto);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (NotUniqueIsbnException e) {
            return new ResponseEntity<>("", HttpStatus.CONFLICT);
        }

    }

    @CrossOrigin
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody CatalogDto catalogDto ){
        try {
            catalogService.update(catalogDto);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }

    }
    @CrossOrigin
    @GetMapping("/isbn")
    public ResponseEntity<?> getBooksByIsbn(@RequestParam("isbn") Long isbn){
        try {
            CatalogDto find = catalogService.getByIsbn(isbn);
            return ResponseEntity.ok(find);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin
    @GetMapping("/udk")
    public ResponseEntity<?> getBooksByUdk(@RequestParam("udk") String udk){
        try {
            List<CatalogDto> catalogDtoList = catalogService.getByUdk(udk);
            return ResponseEntity.ok(catalogDtoList);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping("/author")
    public ResponseEntity<?> getByAuthor(@RequestParam("author") String author){
        try {
            List<CatalogDto> catalogDtoList = catalogService.getByAuthor(author);
            return ResponseEntity.ok(catalogDtoList);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }
    @CrossOrigin
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("isbn") Long isbn){
        try {
             catalogService.delete(isbn);
            return new ResponseEntity<>("",HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }



}
