package org.example.solution.service;

import org.example.solution.dto.BookDto;
import org.example.solution.dto.BookSearchCriteria;

import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    
    BookDto getBookById(String id);
    
    BookDto createBook(BookDto bookDto);
    
    BookDto updateBook(BookDto bookDto, String id);
    
    void deleteBook(String id);
    
    List<BookDto> getBooksByAuthor(String author);
    
    List<BookDto> getAvailableBooksByGenre(String genre);
    
    List<BookDto> getBooksByTag(String tag);
    
    BookDto toggleAvailability(String id);
    
    // Additional service methods for practice queries
    List<BookDto> getBooksByYearAndLater(Integer year);
    
    List<BookDto> getBooksByTitleContaining(String titlePart);
    
    List<BookDto> getAvailableBooksWithMinRating(Integer minRating);

    // בתוך ממשק BookService
    List<BookDto> searchBooks(BookSearchCriteria criteria);
}