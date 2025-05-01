package org.example.stage3.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentRepositoryExtension {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void deleteByStudentId(String studentId) {
        Query query = new Query(Criteria.where("studentId").is(studentId));
        mongoTemplate.remove(query, "enrollments");
    }

    public void deleteByCourseId(String courseId) {
        Query query = new Query(Criteria.where("courseId").is(courseId));
        mongoTemplate.remove(query, "enrollments");
    }
}