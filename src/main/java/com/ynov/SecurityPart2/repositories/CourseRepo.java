package com.ynov.SecurityPart2.repositories;

import com.ynov.SecurityPart2.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    Course save (Course course);
    Optional<Course> findById(Long id);
    List<Course> findAll();
}
