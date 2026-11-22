package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void testStudentListConstructorsAndGetters() {
        StudentList s1 = new StudentList("John Doe");
        assertEquals("John Doe", s1.getName());

        StudentList s2 = new StudentList(101, "Jane Smith");
        assertEquals(101, s2.getID());
        assertEquals("Jane Smith", s2.getName());

        s2.setName("Jane Doe");
        assertEquals("Jane Doe", s2.getName());

        s2.setID(202);
        assertEquals(202, s2.getID());
        assertEquals("ID: 202 | Name: Jane Doe", s2.toString());
    }
}
