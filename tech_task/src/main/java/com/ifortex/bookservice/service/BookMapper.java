package com.ifortex.bookservice.service;

import com.ifortex.bookservice.model.Book;

import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setDescription(rs.getString("description"));
        LocalDateTime localDateTime = rs.getObject("publication_date", LocalDateTime.class);
        book.setPublicationDate(localDateTime);
        Array genreArray = rs.getArray("genre");
        if (genreArray != null) {
            String[] genres = (String[]) genreArray.getArray();
            book.setGenres(new HashSet<>(Arrays.asList(genres)));
        } else {
            book.setGenres(new HashSet<>());
        }
        return book;
    }
}