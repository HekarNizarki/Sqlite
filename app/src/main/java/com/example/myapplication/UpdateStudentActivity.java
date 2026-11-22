package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Secondary Activity responsible for updating or deleting a selected student record.
 */
public class UpdateStudentActivity extends AppCompatActivity {

    // Database helper instance
    private DatabaseHandler db;

    // Data passed from MainActivity
    private int studentId;
    private String studentName;

    // View references
    private EditText etUpdateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_update_student);

        // Apply system bar insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_update), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database handler
        db = new DatabaseHandler(this);

        // Retrieve student ID and Name passed via Intent extras
        Intent intent = getIntent();
        studentId = intent.getIntExtra("STUDENT_ID", -1);
        studentName = intent.getStringExtra("STUDENT_NAME");

        // Bind layout views
        TextView tvStudentId = findViewById(R.id.tvStudentId);
        etUpdateName = findViewById(R.id.etUpdateName);

        Button btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
        Button btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
        Button btnCancel = findViewById(R.id.btnCancel);

        // Display retrieved values in UI
        tvStudentId.setText("Student ID: " + studentId);
        if (studentName != null) {
            etUpdateName.setText(studentName);
        }

        // Handle Save Update button click
        btnSaveUpdate.setOnClickListener(v -> {
            String updatedName = etUpdateName.getText().toString().trim();
            if (updatedName.isEmpty()) {
                Toast.makeText(UpdateStudentActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            // Update student in SQLite and return to MainActivity
            StudentList student = new StudentList(studentId, updatedName);
            db.updateStudent(student);
            Toast.makeText(UpdateStudentActivity.this, "Student updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Handle Delete button click
        btnDeleteStudent.setOnClickListener(v -> {
            // Delete student from SQLite and return to MainActivity
            StudentList student = new StudentList(studentId, studentName);
            db.deleteStudent(student);
            Toast.makeText(UpdateStudentActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Handle Cancel button click: close screen without saving changes
        btnCancel.setOnClickListener(v -> finish());
    }
}

