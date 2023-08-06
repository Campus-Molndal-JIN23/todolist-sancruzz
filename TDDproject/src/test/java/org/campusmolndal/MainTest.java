package org.campusmolndal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class MainTest {
    Todo todo;
    @BeforeEach
    void setUp() {
        todo = new Todo("test");
        todo.setCompleted(true);
        todo.setId(1);
    }
    @Test
    void testGetId() {

        int e = 1;
        assertEquals(e, todo.getId());
    }

}