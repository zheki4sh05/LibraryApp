package ru.digitalchief.LibraryApp.dao;

import jakarta.persistence.*;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.digitalchief.LibraryApp.entity.*;

import java.util.*;

@Repository
public class CatalogDao {

    @Autowired
    private EntityManager entityManager;

    public Optional<Book> getByUsbn(Long number){
        Session currentSession = entityManager.unwrap(Session.class);
        Book book = currentSession.get(Book.class, number);
        return Optional.of(book);
    }


}
