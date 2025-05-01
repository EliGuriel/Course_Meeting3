package org.example.solution.dto;

import lombok.Data;

@Data
public class BookSearchCriteria {
    private String author;
    private Integer publishYearFrom;
    private Integer publishYearTo;
    private String genre;
    private Boolean available;
    private String titleContains;
    private Integer minRating;
}