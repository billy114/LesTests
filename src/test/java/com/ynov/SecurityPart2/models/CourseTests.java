package com.ynov.SecurityPart2.models;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseTests {
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    public void shouldSuccessWithValidTitleAndValidContent(){
        Course course = new Course(null, "Title", "Valid Content");
        Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldThrowConstraintsValidationWithTitleIsBlank(){
        Course course = new Course(null, "", "Valid Content");
        Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertFalse(violations.isEmpty());

        boolean found = false;
        for (ConstraintViolation<Course> violation : violations)
            if (violation.getMessage().equals("Title is required")) {
                found = true;
                break;
            }
        assertTrue(found);
    }

    @Test
    public void shouldThrowConstraintsValidationWithContentIsBlank(){
        Course course = new Course(null, "Title", "");
        Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertFalse(violations.isEmpty());
        boolean found = false;
        for (ConstraintViolation<Course> violation : violations)
            if (violation.getMessage().equals("Content is required")) {
                found = true;
                break;
            }
        assertTrue(found);
    }

    @Test
    public void shouldThrowConstraintsValidationWithContentIsLessThan10(){
        Course course = new Course(null, "Title", "Content");
        Set<ConstraintViolation<Course>> violations = validator.validate(course);
        assertFalse(violations.isEmpty());
        boolean found = false;
        for (ConstraintViolation<Course> violation : violations)
            if (violation.getMessage().equals("Content must be at least 10 characters")) {
                found = true;
                break;
            }
        assertTrue(found);
    }
}
