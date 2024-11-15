package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookMapper;
import com.ifortex.bookservice.service.BookService;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
public class BookServiceImpl implements BookService {
    JdbcTemplate jdbcTemplate;

    public BookServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, Long> getBooks() {
        Map<String, Long> result = new LinkedHashMap<>();
        Map<String, Long> books = new HashMap<>();
        List<String> listGenre = jdbcTemplate.queryForList("SELECT unnest(genre) AS genre FROM books", String.class);
        for (int i = 0; i < listGenre.size(); i++) {
            if (books.isEmpty()) {
                books.put(listGenre.get(i), 1L);
            } else if (books.containsKey(listGenre.get(i))) {
                books.replace(listGenre.get(i), books.get(listGenre.get(i)) + 1L);
            } else {
                books.put(listGenre.get(i), 1L);
            }
        }
        books.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    @Override
    public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria == null || isSearchCriteriaEmpty(searchCriteria)) {
            return jdbcTemplate.query("SELECT * FROM books ORDER BY publication_date", new BookMapper());
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (searchCriteria.getTitle() != null && !searchCriteria.getTitle().isEmpty()) {
            sql.append(" AND title ILIKE ?");
            params.add("%" + searchCriteria.getTitle() + "%");
        }
        if (searchCriteria.getAuthor() != null && !searchCriteria.getAuthor().isEmpty()) {
            sql.append(" AND author ILIKE ?");
            params.add("%" + searchCriteria.getAuthor() + "%");
        }
        if (searchCriteria.getGenre() != null && !searchCriteria.getGenre().isEmpty()) {
            sql.append(" AND EXISTS (SELECT 1 FROM unnest(genre) AS g WHERE g ILIKE ?)");
            params.add("%" + searchCriteria.getGenre() + "%");
        }
        if (searchCriteria.getDescription() != null && !searchCriteria.getDescription().isEmpty()) {
            sql.append(" AND description ILIKE ?");
            params.add("%" + searchCriteria.getDescription() + "%");
        }
        if (searchCriteria.getYear() != null) {
            sql.append(" AND EXTRACT(YEAR FROM publication_date) = ?");
            params.add(searchCriteria.getYear());
        }
        sql.append(" ORDER BY publication_date");
        return jdbcTemplate.query(sql.toString(), params.toArray(), new BookMapper());
    }

    private boolean isSearchCriteriaEmpty(SearchCriteria searchCriteria) {
        return (searchCriteria.getTitle() == null || searchCriteria.getTitle().isEmpty()) &&
                (searchCriteria.getAuthor() == null || searchCriteria.getAuthor().isEmpty()) &&
                (searchCriteria.getGenre() == null || searchCriteria.getGenre().isEmpty()) &&
                (searchCriteria.getDescription() == null || searchCriteria.getDescription().isEmpty()) &&
                searchCriteria.getYear() == null;
    }
}