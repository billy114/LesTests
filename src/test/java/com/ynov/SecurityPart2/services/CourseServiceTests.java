package com.ynov.SecurityPart2.services;

import com.ynov.SecurityPart2.models.Course;
import com.ynov.SecurityPart2.repositories.CourseRepo;
import com.ynov.SecurityPart2.servicesImplem.CourseImplem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTests {

    @Mock
    CourseRepo courseRepo;

    @InjectMocks
    CourseImplem courseImplem;

    @Test
    public void testCreateCourse(){
        Long courseId = 1L;
        String title = "Title";
        String content = "Valid content";
        Course mockCourse = new Course(courseId, title, content);
        when(courseRepo.save(any(Course.class))).thenReturn(mockCourse);
        Course createdCourse = courseImplem.createCourse(
                new Course(null , title, content)
        );
        assertEquals(mockCourse, createdCourse);
    }

    @Test
    public void testGetCourseByIdWhenCourseExist (){
        Long courseId = 1L;
        String title = "Title";
        String content = "Valid content";
        Course mockCourse = new Course(courseId, title, content);
        when(courseRepo.findById(any(Long.class))).thenReturn(Optional.of(mockCourse));
        Optional<Course> foundCourse = courseImplem.getCourseById(courseId);
        assertTrue(foundCourse.isPresent());
        assertEquals(title, foundCourse.get().getTitle());
        assertEquals(content, foundCourse.get().getContent());
    }

    @Test
    public void testGetCourseByIdWhenCourseNotExist (){
        when(courseRepo.findById(any(Long.class))).thenReturn(Optional.empty());
        Optional<Course> foundCourse = courseImplem.getCourseById(1L);
        assertTrue(foundCourse.isEmpty());
    }

    @Test
    public void testGetAllCourseWhenTableIsNotEmpty(){
        List<Course> mockCourses = List.of(
                new Course(1L, "Java", "Cours de java"),
                new Course(1L, "Spring", "Cours de spring")
        );
        when(courseRepo.findAll()).thenReturn(mockCourses);
        List<Course> courses = courseImplem.getAllCourses();
        assertEquals(2, courses.size());
        assertEquals(mockCourses, courses);
    }

    @Test
    public void testGetAllCourseWhenTableIsEmpty(){
        when(courseRepo.findAll()).thenReturn(new ArrayList<>());
        List<Course> courses = courseImplem.getAllCourses();
        assertTrue(courses.isEmpty());
    }
}
