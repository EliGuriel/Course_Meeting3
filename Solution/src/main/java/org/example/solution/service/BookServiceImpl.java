package org.example.solution.service;


import org.example.solution.collection.Book;
import org.example.solution.dto.BookDto;
import org.example.solution.exception.ResourceNotFoundException;
import org.example.solution.mappper.BookMapper;
import org.example.solution.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }
    
    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDtoList(books);
    }
    
    @Override
    public BookDto getBookById(String id) {
        Book book = getBookEntityById(id);
        return bookMapper.toDto(book);
    }
    
    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }
    
    @Override
    public BookDto updateBook(BookDto bookDto, String id) {
        Book existingBook = getBookEntityById(id);
        bookMapper.updateEntityFromDto(existingBook, bookDto);
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook);
    }
    
    @Override
    public void deleteBook(String id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
    
    @Override
    public List<BookDto> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthor(author);
        return bookMapper.toDtoList(books);
    }
    
    @Override
    public List<BookDto> getAvailableBooksByGenre(String genre) {
        List<Book> books = bookRepository.findByGenreAndAvailableTrue(genre);
        return bookMapper.toDtoList(books);
    }
    
    @Override
    public List<BookDto> getBooksByTag(String tag) {
        List<Book> books = bookRepository.findBooksByTag(tag);
        return bookMapper.toDtoList(books);
    }
    
    @Override
    public BookDto toggleAvailability(String id) {
        Book book = getBookEntityById(id);
        book.setAvailable(!book.getAvailable());
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }
    
    @Override
    public List<BookDto> getBooksByYearAndLater(Integer year) {
        List<Book> books = bookRepository.findByPublishYearGreaterThanEqual(year);
        return bookMapper.toDtoList(books);
    }
    
    @Override
    public List<BookDto> getBooksByTitleContaining(String titlePart) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(titlePart);
        return bookMapper.toDtoList(books);
    }
    
    @Override
    public List<BookDto> getAvailableBooksWithMinRating(Integer minRating) {
        List<Book> books = bookRepository.findAvailableBooksWithMinRating(minRating);
        return bookMapper.toDtoList(books);
    }
    
    private Book getBookEntityById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}