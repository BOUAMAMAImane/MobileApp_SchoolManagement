package com.example.mytest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    private ListView studentListView;
    private ArrayAdapter<String> studentAdapter;
    private List<String> studentList;
    private DatabaseHelper databaseHelper;
    private int subjectId;
    private List<String> selectedStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

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
                    boolean success = addStudentsToSubject(subjectId, selectedStudents);

                    if (success) {
                        Toast.makeText(AddStudentActivity.this, "Etudiant ajoute avec succces", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddStudentActivity.this, "Echec d ajouter l etudiant", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddStudentActivity.this, "Aucun etudiant selectionner", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private List<String> getStudentList() {
        List<String> studentList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_EMAIL +
                " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COL_USERTYPE + " = 'Etudiant'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String studentEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL));
                studentList.add(studentEmail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return studentList;
    }
    private int getStudentId(String studentEmail) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_ID +
                " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COL_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{studentEmail});

        int studentId = -1;
        if (cursor.moveToFirst()) {
            studentId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
        }

        cursor.close();
        return studentId;
    }

    private boolean addStudentsToSubject(int subjectId, List<String> students) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean success = true;

        for (String student : students) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_SUBJECT_ID, subjectId);
            int studentId = getStudentId(student);
            values.put(DatabaseHelper.COL_STUDENT_ID, studentId);


            long result = db.insert(DatabaseHelper.TABLE_STUDENT_SUBJECT, null, values);
            if (result == -1) {
                success = false;
            }
        }

        return success;
    }

}



