package com.example.mytest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    private ListView studentListView;
    private ArrayAdapter<String> studentAdapter;
    private List<String> studentList;
    private DatabaseHelper databaseHelper;
    private int subjectId;
    private List<String> selectedStudents;
    private long absenceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentListView = findViewById(R.id.studentListView);
        databaseHelper = new DatabaseHelper(this);

        subjectId = getIntent().getIntExtra("subjectId", -1);

        studentList = getStudentList();
        selectedStudents = new ArrayList<>();

        studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, studentList);
        studentListView.setAdapter(studentAdapter);
        studentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStudent = studentList.get(position);

                if (studentListView.isItemChecked(position)) {
                    selectedStudents.add(selectedStudent);
                } else {
                    selectedStudents.remove(selectedStudent);
                }
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedStudents.isEmpty()) {
                    boolean success = markStudentsAbsent(selectedStudents);
                    long rowId = -1;

                    if (success) {
                        Toast.makeText(StudentListActivity.this, "Students added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StudentListActivity.this, DetailsActivity.class);
                        intent.putStringArrayListExtra("selectedStudents", (ArrayList<String>) selectedStudents);
                        intent.putExtra("subjectId", subjectId);
                        startActivity(intent);
                        if (!selectedStudents.isEmpty()) {
                            rowId = absenceId;
                        }

                        intent.putExtra("absenceId", rowId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(StudentListActivity.this, "Failed to add students", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudentListActivity.this, "No students selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        private List<String> getStudentList() {
        List<String> studentList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_EMAIL +
                " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COL_ID + " IN (SELECT " + DatabaseHelper.COL_STUDENT_ID +
                " FROM " + DatabaseHelper.TABLE_STUDENT_SUBJECT +
                " WHERE " + DatabaseHelper.COL_SUBJECT_ID + " = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(subjectId)});

        if (cursor.moveToFirst()) {
            do {
                String studentEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL));
                studentList.add(studentEmail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return studentList;
    }

    private boolean markStudentsAbsent(List<String> studentList) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean success = false;

        for (String studentEmail : studentList) {
            String studentIdQuery = "SELECT " + DatabaseHelper.COL_ID +
                    " FROM " + DatabaseHelper.TABLE_USERS +
                    " WHERE " + DatabaseHelper.COL_EMAIL + " = ?";
            Cursor studentIdCursor = db.rawQuery(studentIdQuery, new String[]{studentEmail});

            if (studentIdCursor.moveToFirst()) {
                int studentId = studentIdCursor.getInt(studentIdCursor.getColumnIndex(DatabaseHelper.COL_ID));

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COL_STUDENT_ID, studentId);
                values.put(DatabaseHelper.COL_SUBJECT_ID, subjectId);
                values.put(DatabaseHelper.COL_ABSENT, "1");

                long rowId = db.insert(DatabaseHelper.TABLE_STUDENT_SUBJECT, null, values);

                if (rowId != -1) {
                    success = true;
                    absenceId = rowId; // Assigner la valeur de rowId à absenceId
                }
            }

            studentIdCursor.close();
        }

        return success;
    }

}
