package com.example.mytest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ProfessorSubjectsActivity extends AppCompatActivity {
    private EditText subjectEditText;
    private Button addSubjectButton;
    private DatabaseHelper databaseHelper;
    private String userEmail;
    private ListView subjectListView;
    private ArrayAdapter<String> subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_professor_subjects);
        subjectEditText = findViewById(R.id.subjectEditText);
        addSubjectButton = findViewById(R.id.addSubjectButton);

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        int professorId = intent.getIntExtra("professorId", -1); // Récupérer l'ID du professeur depuis l'intent

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectEditText.getText().toString();

                if (subject.isEmpty()) {
                    Toast.makeText(ProfessorSubjectsActivity.this, "Entrer une matiere svp", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = addSubject(professorId, subject);
                    if (success) {
                        Toast.makeText(ProfessorSubjectsActivity.this, "Matiere ajoutee avec succees ", Toast.LENGTH_SHORT).show();
                        subjectEditText.setText("");

                        Intent intent = new Intent(ProfessorSubjectsActivity.this, AddStudentActivity.class);
                        intent.putExtra("subjectId", getSubjectId(subject)); // Passer l'ID de la matière à l'activité suivante en tant qu'entier
                        startActivity(intent);

                    } else {
                        Toast.makeText(ProfessorSubjectsActivity.this, "Echec d ajouter matiere", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean addSubject(int professorId, String subject) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_SUBJECT, subject);
        values.put(DatabaseHelper.COL_PROFESSOR_ID, professorId);
        long result = db.insert(DatabaseHelper.TABLE_SUBJECTS, null, values);
        return result != -1;
    }

    public int getSubjectId(String subjectName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_ID  + " FROM " + DatabaseHelper.TABLE_SUBJECTS +
                " WHERE " + DatabaseHelper.COL_SUBJECT + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{subjectName});
        int subjectId = -1;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.COL_ID);
            if (columnIndex != -1) {
                subjectId = cursor.getInt(columnIndex);
            }
        }
        cursor.close();
        return subjectId;
    }
}