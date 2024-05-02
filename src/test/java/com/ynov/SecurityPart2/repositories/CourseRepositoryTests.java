package com.ynov.SecurityPart2.repositories;

import com.ynov.SecurityPart2.models.Course;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CourseRepositoryTests {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CourseRepo courseRepo;

    @Test
    public void shouldSave(){
        String title = "Title";
        String content = "Valid Content";
        Course entity = new Course(null, title, content);
        Course savedCourse = courseRepo.save(entity);
        assertThat(savedCourse.getId()).isNotNull();
        assertEquals(savedCourse.getTitle(), entity.getTitle());
        assertEquals(savedCourse.getContent(), entity.getContent());
    }

    @Test
    public void shouldSaveAndFind(){
        Course savedCourse = entityManager.persistFlushFind(
                new Course(null, "Title", "Valid Content")
        );
        Optional<Course> foundCourse = courseRepo.findById(savedCourse.getId());
        assertTrue(foundCourse.isPresent());
        assertEquals(savedCourse, foundCourse.get());
    }

    @Test
    public void shouldFailToSaveWhenTitleIsBlank(){
        assertThrows( jakarta.validation.ConstraintViolationException.class, () -> {
            Course savedCourse = entityManager.persistFlushFind(
                    new Course(null, "", "Valid Content")
            );
        });
    }

    @Test
    public void shouldReturnList(){
        entityManager.persist(
                new Course(null, "Java", "Valid Content")
        );
        entityManager.persist(
                new Course(null, "Spring", "Valid Content")
        );
        entityManager.flush();
        List<Course> courses = courseRepo.findAll();
        assertEquals(courses.size(), 2);
        assertThat(courses).extracting(Course::getTitle)
                .containsExactly("Java", "Spring");
    }
}
