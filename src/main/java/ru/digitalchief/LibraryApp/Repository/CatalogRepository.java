package ru.digitalchief.LibraryApp.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;
import ru.digitalchief.LibraryApp.entity.*;

import java.util.*;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    @Query("""

        select c from Catalog c
        where c.id = :isbn
    
""")
    Optional<Catalog> getByUsbn(@Param("isbn") Long isbn);


    @Query("""

        select c from Catalog c
        where c.author = :author
    
""")
    List<Catalog> findByAuthor(@Param("author")String author);
    @Query("""

        select c from Catalog c
        where c.udk = :udk
    
""")
    List<Catalog> findByUdk(@Param("udk")String udk);
}
