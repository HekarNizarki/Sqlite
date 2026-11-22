package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

/**
 * Main Activity displaying the list of students from SQLite, allowing adding new records
 * and tapping existing records to edit or delete them on a secondary screen.
 */
public class MainActivity extends AppCompatActivity {

    // Helper instance for database operations
    private DatabaseHandler db;

    // UI View references
    private EditText etStudentName;
    private TextView tvStudentCount;
    private ListView lvStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        // Apply system bar window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the database helper
        db = new DatabaseHandler(this);

        // Bind layout views
        etStudentName = findViewById(R.id.etStudentName);
        tvStudentCount = findViewById(R.id.tvStudentCount);
        lvStudents = findViewById(R.id.lvStudents);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Seed initial sample data if database is empty on first app start
        if (db.getStudentListCount() == 0) {
            Log.d("Insert: ", "Inserting initial students...");
            db.addStudent(new StudentList("A"));
            db.addStudent(new StudentList("B"));
            db.addStudent(new StudentList("C"));
            db.addStudent(new StudentList("D"));
        }

        // Load and display initial student list
        refreshStudentList();

        // Handle item click: launch UpdateStudentActivity with selected student details
        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            StudentList clickedStudent = (StudentList) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, UpdateStudentActivity.class);
            intent.putExtra("STUDENT_ID", clickedStudent.getID());
            intent.putExtra("STUDENT_NAME", clickedStudent.getName());
            startActivity(intent);
        });

        // Handle Add button click: insert new student record
        btnAdd.setOnClickListener(v -> {
            String name = etStudentName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter student name", Toast.LENGTH_SHORT).show();
                return;
            }
            db.addStudent(new StudentList(name));
            Toast.makeText(MainActivity.this, "Student added", Toast.LENGTH_SHORT).show();
            etStudentName.setText("");
            refreshStudentList();
        });
    }

    // Called whenever this Activity returns to the foreground (e.g. back from Update screen)
    @Override
    protected void onResume() {
        super.onResume();
        refreshStudentList();
    }

    // Helper method to reload all student records from SQLite and refresh the ListView UI
    private void refreshStudentList() {
        List<StudentList> students = db.getAllStudentList();
        ArrayAdapter<StudentList> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        lvStudents.setAdapter(adapter);
        tvStudentCount.setText("Total Students: " + db.getStudentListCount());
    }
}


