package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite Database Helper class responsible for database creation, structure updates,
 * and CRUD (Create, Read, Update, Delete) operations on the students table.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database configuration constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "studentlist";
    private static final String TABLE_STUDENTS = "students";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    // Constructor to pass context and database details to SQLiteOpenHelper
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the students table with auto-incrementing ID
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_STUDENTS_TABLE);
    }

    // Called when upgrading the database (e.g. incrementing DATABASE_VERSION)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);

        // Recreate tables
        onCreate(db);
    }

    // CREATE: Inserts a new student record into the database
    public void addStudent(StudentList student) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create key-value pairs matching column names and values
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName());

        // Insert row and close database connection
        db.insert(TABLE_STUDENTS, null, values);
        db.close();
    }

    // READ (Single Record): Fetches a single student by ID
    public StudentList getStudent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query table for matching ID
        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[] { KEY_ID, KEY_NAME }, KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);

        StudentList student = null;
        if (cursor.moveToFirst()) {
            student = new StudentList(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        cursor.close();

        return student;
    }

    // READ (All Records): Fetches all student records into a List
    public List<StudentList> getAllStudentList() {
        List<StudentList> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Iterate through cursor rows and populate list
        if (cursor.moveToFirst()) {
            do {
                StudentList student = new StudentList();
                student.setID(Integer.parseInt(cursor.getString(0)));
                student.setName(cursor.getString(1));
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return studentList;
    }

    // UPDATE: Updates an existing student's details in the database
    public int updateStudent(StudentList student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, student.getName());

        // Update matching row by ID
        int count = db.update(TABLE_STUDENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(student.getID()) });
        db.close();
        return count;
    }

    // DELETE: Removes a single student from the database
    public void deleteStudent(StudentList student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(student.getID()) });
        db.close();
    }

    // COUNT: Returns the total count of student records
    public int getStudentListCount() {
        String countQuery = "SELECT * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

}


