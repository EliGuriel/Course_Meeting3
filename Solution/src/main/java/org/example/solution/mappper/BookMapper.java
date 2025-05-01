package org.example.solution.mappper;

import org.example.solution.collection.Book;
import org.example.solution.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book toEntity(BookDto dto) {
        if (dto == null) {
            return null;
        }
        
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublishYear(dto.getPublishYear());
        book.setGenre(dto.getGenre());
        book.setAvailable(dto.getAvailable());
        book.setTags(dto.getTags());
        book.setRating(dto.getRating());
        
        return book;
    }
    
    public BookDto toDto(Book entity) {
        if (entity == null) {
            return null;
        }
        
        BookDto dto = new BookDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setPublishYear(entity.getPublishYear());
        dto.setGenre(entity.getGenre());
        dto.setAvailable(entity.getAvailable());
        dto.setTags(entity.getTags());
        dto.setRating(entity.getRating());
        
        return dto;
    }
    
    public List<BookDto> toDtoList(List<Book> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public void updateEntityFromDto(Book entity, BookDto dto) {
        if (entity == null || dto == null) {
            return;
        }
        
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setPublishYear(dto.getPublishYear());
        entity.setGenre(dto.getGenre());
        entity.setAvailable(dto.getAvailable());
        entity.setTags(dto.getTags());
        entity.setRating(dto.getRating());
    }
}