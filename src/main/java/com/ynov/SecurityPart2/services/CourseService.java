package com.ynov.SecurityPart2.services;

import com.ynov.SecurityPart2.models.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Optional<Course> getCourseById(Long id);

    List<Course> getAllCourses();

    Course createCourse(Course course);
}
