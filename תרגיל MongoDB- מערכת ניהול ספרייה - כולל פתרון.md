<div dir="rtl">

# תרגיל MongoDB: מערכת ניהול ספרייה

## מטרת התרגיל
בתרגיל זה תיצור אפליקציית Spring Boot שמשתמשת ב-MongoDB לניהול מידע על ספרים בספרייה.

## דרישות המערכת
1. יצירת מודל `Book` שמייצג ספר בספרייה
2. יצירת Repository עם שאילתות נפוצות
3. יצירת Service לביצוע פעולות לוגיות
4. יצירת Controller לחשיפת REST API

## שלב 1: הגדרת המודל (Entity)

יש ליצור מחלקת `Book` שמייצגת ספר בספרייה עם השדות הבאים:
- מזהה (`id`) - מזהה ייחודי של הספר
- שם הספר (`title`) - חובה
- מחבר (`author`) - חובה
- שנת פרסום (`publishYear`) - לא חובה
- ז'אנר (`genre`) - חובה
- זמין להשאלה (`available`) - ברירת מחדל: `true`
- מילות מפתח (`tags`) - מערך של מחרוזות (לא חובה)
- דירוג (`rating`) - מספר בין 1 ל-5 (לא חובה)

</div>

```java
package com.example.library.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Author is required")
    private String author;
    
    private Integer publishYear;
    
    @NotBlank(message = "Genre is required")
    private String genre;
    
    @NotNull(message = "Availability status is required")
    private Boolean available = true;
    
    private List<String> tags;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Integer rating;
}
```

<div dir="rtl">

## שלב 2: יצירת DTO

</div>

```java
package com.example.library.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Author is required")
    private String author;
    
    private Integer publishYear;
    
    @NotBlank(message = "Genre is required")
    private String genre;
    
    @NotNull(message = "Availability status is required")
    private Boolean available = true;
    
    private List<String> tags;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Integer rating;
}
```

<div dir="rtl">

## שלב 3: יצירת BookRepository

יש ליצור ממשק BookRepository שמשתמש ב-MongoRepository ולהוסיף שלוש שאילתות:

</div>

```java
package com.example.library.repository;

import com.example.library.collection.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    
    // 1. מציאת ספרים לפי מחבר
    List<Book> findByAuthor(String author);
    
    // 2. מציאת ספרים לפי ז'אנר שזמינים להשאלה
    List<Book> findByGenreAndAvailableTrue(String genre);
    
    // 3. מציאת ספרים על פי תג (מילת מפתח)
    @Query("{ 'tags': ?0 }")
    List<Book> findBooksByTag(String tag);
}
```

<div dir="rtl">

## שלב 4: יצירת Mapper

</div>

```java
package com.example.library.mapper;

import com.example.library.collection.Book;
import com.example.library.dto.BookDto;
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
```


<div dir="rtl">

## שלב 5: יצירת service

</div>

```java
package com.example.library.service;

import com.example.library.dto.BookDto;

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
}
```

<div dir="rtl">

וכעת המימוש של ה-service:

</div>

```java
package com.example.library.service;

import com.example.library.collection.Book;
import com.example.library.dto.BookDto;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.BookRepository;
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
    
    private Book getBookEntityById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}
```

<div dir="rtl">

## שלב 6: יצירת controller

</div>

```java
package com.example.library.controller;

import com.example.library.dto.BookDto;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
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
}
```

<div dir="rtl">

## שלב 7: יצירת GlobalExceptionHandler

</div>

```java
package com.example.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Resource Not Found", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Failed",
                errors.toString()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

<div dir="rtl">

## שלב 8: יצירת ErrorResponse

</div>

```java
package com.example.library.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
```

<div dir="rtl">

## שלב 9: הגדרת ResourceNotFoundException

</div>

```java
package com.example.library.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

<div dir="rtl">

## שלב 10: הגדרת MongoDb בקובץ application.properties

</div>

```properties
spring.application.name=library-service

# MongoDB Configuration
spring.data.mongodb.authentication-database=admin
spring.data.mongodb.database=library
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.username=admin
spring.data.mongodb.password=admin
```

<div dir="rtl">

## משימות לתרגול

לאחר יצירת הפרויקט, השלם את המשימות הבאות:

1. הוסף שאילתה חדשה ל-`BookRepository` שמחזירה ספרים משנת פרסום מסוימת ומעלה.

</div>
   ```java
   List<Book> findByPublishYearGreaterThanEqual(Integer year);
   ```

<div dir="rtl">

2. הוסף שאילתה שמחפשת ספרים לפי חלק מהכותרת (ללא התחשבות ברישיות).
</div>
   ```java
   List<Book> findByTitleContainingIgnoreCase(String titlePart);
   ```

<div dir="rtl">

3. הוסף שאילתת MongoDB Query מותאמת אישית שמחזירה ספרים עם דירוג גבוה מערך מסוים ושזמינים להשאלה.

</div>
   ```java
   @Query("{ 'rating': { $gte: ?0 }, 'available': true }")
   List<Book> findAvailableBooksWithMinRating(Integer minRating);
   ```

<div dir="rtl">

4. עדכן את ה-Service וה-Controller כדי לתמוך בשאילתות החדשות.

## הנחיות להרצה

1. וודא ש-MongoDB פועל על המחשב שלך (או דרך Docker)
2. הרץ את אפליקציית Spring Boot
3. השתמש ב-Postman או כלי דומה לבדיקת ה-API:
    - POST `/api/books` - ליצירת ספר חדש
    - GET `/api/books` - לקבלת כל הספרים
    - GET `/api/books/{id}` - לקבלת ספר לפי מזהה
    - PUT `/api/books/{id}` - לעדכון ספר
    - DELETE `/api/books/{id}` - למחיקת ספר
    - GET `/api/books/author/{author}` - לקבלת ספרים לפי מחבר
    - GET `/api/books/genre/{genre}/available` - לקבלת ספרים זמינים לפי ז'אנר
    - GET `/api/books/tag/{tag}` - לקבלת ספרים לפי תג

## בונוס

1. הוסף אפשרות לחיפוש מתקדם של ספרים לפי מספר קריטריונים (מחבר, ז'אנר, שנת פרסום, זמינות)
2. הוסף מערכת השאלות ספרים שמאפשרת לנהל מי השאיל איזה ספר ומתי
3. הוסף מערכת דירוג והמלצות לספרים

</div>