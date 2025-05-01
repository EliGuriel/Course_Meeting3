package org.example.solution.repository;

import org.example.solution.collection.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    /*
        1. Find books by author
        2. Find books by genre that are available
        3. Find books by tag, this is a custom query
        4. A Repository method that finds books by tags
     */

    // 1. Find books by author
    List<Book> findByAuthor(String author);

    // 2. Find books by genre that are available
    List<Book> findByGenreAndAvailableTrue(String genre);


    // 3. Find books by tag, this is a custom query
    @Query("{ 'tags': ?0 }")
    List<Book> findBooksByTag(String tag);

    // this is a Repository method that finds books by tags
    List<Book> findAllByTagsIn(Collection<List<String>> tags);


    /* Additional queries for practice
        1. Find books by publication year and later
        2. Find books by partial title (case-insensitive)
        3. Custom MongoDB query for available books with minimum rating
        4. A Repository method that finds books by rating
     */
    // Find books by publication year and later
    List<Book> findByPublishYearGreaterThanEqual(Integer year);

    // Find books by partial title (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String titlePart);


    // Custom MongoDB query for available books with minimum rating
    @Query("{ 'rating': { $gte: ?0 }, 'available': true }")
    List<Book> findAvailableBooksWithMinRating(Integer minRating);

    List<Book> findAllByRating(Integer rating);
}