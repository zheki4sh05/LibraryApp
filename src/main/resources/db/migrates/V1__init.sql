CREATE TYPE book_enum as enum ('FREE', 'BORROW');
CREATE CAST (varchar AS book_enum) WITH INOUT AS IMPLICIT;
create table author
(
    id uuid primary key default gen_random_uuid(),
    name character varying(100) unique not null
);

create table book
(
    id uuid primary key default gen_random_uuid(),
    udk character varying not null,
    name character varying(100) unique not null,
    author_id uuid not null,
    constraint author_fk foreign key (author_id)
    references author(id)
    on delete cascade
    on update cascade
);

create table edition
(
    id uuid primary key default gen_random_uuid(),
    isbn character(13) not null unique,
    pages integer not null,
    publication date not null,
    number smallint not null,
    book_id uuid not null,
    constraint book_fk foreign key (book_id)
    references book(id)
    on delete cascade
    on update cascade

);

create table storage
(
    id uuid primary key default gen_random_uuid(),
    status book_enum,
    rack integer not null,
    accounting date not null,
    book_edition_id uuid not null,
    constraint book_edition_fk foreign key (book_edition_id)
    references edition(id)
    on delete cascade
    on update cascade
);







