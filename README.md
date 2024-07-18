# Spring boot, Hibernate, PostgresSQL REST API

A simple Spring boot application that demonstrates the usage of RESTful API using Spring boot, Hibernate and PostgreSQL. 

## Tools and Technologies used

* Java 21
* Spring boot 3.1.1
* PostgresSQL
* JPA
* Hibernate
* Maven
* intellij idea
* Lombok
* org.postgresql.Driver
* Postman
* Git

## Steps to install

**1. Clone the application**

```bash
git clone https://github.com/zheki4sh05/LibraryApp.git
```

**2. Create PostgresSQL database**

```sql
CREATE DATABASE library
```
	
**3. Create table or Run the SQL script file**

```sql
CREATE TABLE IF NOT EXISTS public.book
(
    "number" integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    rack smallint,
    accounting date,
    is_taken boolean,
    catalog_fk bigint NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY ("number"),
    CONSTRAINT fk_category FOREIGN KEY (catalog_fk)
    REFERENCES public.catalog (isbn) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
    )

CREATE TABLE IF NOT EXISTS public.catalog
(
    isbn bigint NOT NULL,
    udk character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default",
    author character varying COLLATE pg_catalog."default",
    pages integer,
    year date,
    CONSTRAINT catalog_pk PRIMARY KEY (isbn)
    )
```
	
**4. Change PostgresSQL Username and Password as per your MySQL Installation**
	
+ open `src/main/resources/application.properties` file.

+ change `spring.datasource.username` and `spring.datasource.password` as per your installation
	
**5. Run the app**

You can run the spring boot app by typing the following command -

```bash
mvn spring-boot:run
```

You can also package the application in the form of a `jar` file and then run it like so -

```bash
mvn package
java -jar LibraryApp-0.0.1-SNAPSHOT.jar
```

The server will start on port 8080.
	
## Explore Rest APIs

The app defines following CRUD APIs.

    POST http://localhost:8080/catalog/create
    
    PUT http://localhost:8080/catalog/update
    
    GET http://localhost:8080/catalog/isbn?isbn={isbn}
    
    GET http://localhost:8080/catalog/udk?udk={udk}
    
    GET http://localhost:8080/catalog/author?author={author}

    DELETE http://localhost:8080/catalog/delete?isbn={isbn}

    POST http://localhost:8080/book/create?rack={rack}&isbn={isbn}

    PUT http://localhost:8080/book/borrow?isbn={rack}

    PUT http://localhost:8080/book/update

    PUT http://localhost:8080/book/delete?isbn={isbn}

You can test them using postman or any other rest client (see resources directory). 



