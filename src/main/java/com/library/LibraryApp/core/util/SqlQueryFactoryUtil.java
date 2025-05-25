package com.library.LibraryApp.core.util;

import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.*;

public class SqlQueryFactoryUtil {


    public static int calcOffset(Integer page, Integer size){
        return (page) * size;
    }
    public static int calcTotalPages(Long total ,Integer size){
        return (int) Math.ceil((double) total / size);
    }


    public static String createAuthorQuery() {

        return """
                select a.* from author as a
                where (:name = '' or a.name = :name)
                limit :size offset :offset
                """;

    }

    public static String createBookQuery(Pageable pageable) {

        validateSort(pageable, List.of("name"));

        Sort sort = pageable.getSort();
        String orderByClause = sort.stream()
                .map(order -> "b." + order.getProperty() + " " + (order.isAscending() ? "ASC" : "DESC"))
                .collect(Collectors.joining(", "));

        String query = """
            select b.* from book as b
            join author a on a.id = b.author_id
            where (:udk = '' or b.udk = :udk)
            and (:name = '' or b.name like :name)
            and (:author = '' or a.name like :author)
            """;

        if (!orderByClause.isEmpty()) {
            query += " order by " + orderByClause;
        }

        query += " limit :size offset :offset";

        return query;
    }
    public static String createBookQuery() {
        return """
                select b.* from book as b
                join author a on a.id = b.author_id
                where (:udk = '' or b.udk = :udk)
                and (:name = '' or b.name like :name)
                and (:author = '' or a.name like :author)
                limit :size offset :offset
                """;

    }


    public static String createEditionQuery() {
        return """
                select e.* from edition as e
                join book b on b.id = e.book_id
                and (:isbn = '' or e.isbn = :isbn)
                and e.number = :number
                and e.publication = :publication
                and (:name = '' or b.name = :name)
                limit :size offset :offset
                """;
    }


    public static String createStorageQuery() {
        return """
                select * from storage
                where is_taken = :mode
                and accounting between :date_from and :date_to
                and rack = :rack
                limit :size offset :offset
                """;


    }

    private static void validateSort(Pageable pageable, List<String> ALLOWED_SORT_FIELDS) {
        if (pageable.getSort().isSorted()) {
            boolean hasInvalidSort = pageable.getSort().stream()
                    .anyMatch(order -> !ALLOWED_SORT_FIELDS.contains(order.getProperty()));

            if (hasInvalidSort) {
                throw new IllegalArgumentException(
                        "Разрешена сортировка только по полям: " + ALLOWED_SORT_FIELDS);
            }
        }
    }
}
