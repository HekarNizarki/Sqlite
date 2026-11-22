package com.example.myapplication;

import androidx.annotation.NonNull;

/**
 * Model class representing a Student entity stored in the SQLite database.
 */
public class StudentList {
    // Database primary key ID
    private int _id;
    // Student name field
    private String _name;

    // Default empty constructor
    public StudentList() { }

    // Constructor with ID and Name (used when reading from database)
    public StudentList(int id, String name) {
        this._id = id;
        this._name = name;
    }

    // Constructor with Name only (used when creating a new record before ID generation)
    public StudentList(String name) {
        this._name = name;
    }

    // Getter and Setter for Student ID
    public int getID() { return this._id; }
    public void setID(int id) { this._id = id; }

    // Getter and Setter for Student Name
    public String getName() { return this._name; }
    public void setName(String name) { this._name = name; }

    // Formatted string representation displayed in the ListView adapter
    @NonNull
    @Override
    public String toString() {
        return "ID: " + _id + " | Name: " + _name;
    }
}


