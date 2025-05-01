package org.example.solution.controller;

import jakarta.validation.Valid;
import org.example.solution.dto.BookDto;
import org.example.solution.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    
    private final BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable String id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
    
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) {
        BookDto createdBook = bookService.createBook(bookDto);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBook.getId())
                .toUri();
        
        return ResponseEntity.created(location).body(createdBook);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable String id,
            @Valid @RequestBody BookDto bookDto) {
        
        BookDto updatedBook = bookService.updateBook(bookDto, id);
        return ResponseEntity.ok(updatedBook);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@PathVariable String author) {
        List<BookDto> books = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/genre/{genre}/available")
    public ResponseEntity<List<BookDto>> getAvailableBooksByGenre(@PathVariable String genre) {
        List<BookDto> books = bookService.getAvailableBooksByGenre(genre);
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<BookDto>> getBooksByTag(@PathVariable String tag) {
        List<BookDto> books = bookService.getBooksByTag(tag);
        return ResponseEntity.ok(books);
    }
    
    @PatchMapping("/{id}/toggle-availability")
    public ResponseEntity<BookDto> toggleAvailability(@PathVariable String id) {
        BookDto book = bookService.toggleAvailability(id);
        return ResponseEntity.ok(book);
    }
    
    // Additional endpoints for practice queries
    
    @GetMapping("/year/{year}/and-later")
    public ResponseEntity<List<BookDto>> getBooksByYearAndLater(@PathVariable Integer year) {
        List<BookDto> books = bookService.getBooksByYearAndLater(year);
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/search/title")
    public ResponseEntity<List<BookDto>> getBooksByTitleContaining(@RequestParam String query) {
        List<BookDto> books = bookService.getBooksByTitleContaining(query);
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/search/rating")
    public ResponseEntity<List<BookDto>> getAvailableBooksWithMinRating(@RequestParam Integer rating) {
        List<BookDto> books = bookService.getAvailableBooksWithMinRating(rating);
        return ResponseEntity.ok(books);
    }
}