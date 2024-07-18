package ru.digitalchief.LibraryApp.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import ru.digitalchief.LibraryApp.entity.*;

import java.util.*;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
    select b from Book b where b.catalogFk.id = :userIsbn and b.isTaken=false

""")
    Optional<Book> findFirstWithFreeBook(@Param("userIsbn") Long isbn);

    @Query("""

select b from Book b
where b.id = :number

""")

    Book findByNumber(@Param("number") Integer number);
}
