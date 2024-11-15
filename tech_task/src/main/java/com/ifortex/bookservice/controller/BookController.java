package com.ifortex.bookservice.controller;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/statistic")
    public Map<String, Long> getStatistic() {
        return bookService.getBooks();
    }

    @PostMapping("/search-book")
    public List<Book> findBooks(@RequestBody @Nullable SearchCriteria searchCriteria) {
        return bookService.getAllByCriteria(searchCriteria);
    }
}