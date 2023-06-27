package com.example.mytest;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import com.example.mytest.AbsentSubject;

public class EtudiantActivity extends AppCompatActivity {
    private Button viewAbsencesButton;
    private ListView subjectListView;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_etudiant);
        userEmail = getIntent().getStringExtra("userEmail");

        int studentId = getUserIdByEmail(userEmail);
        List<String> subjects = getSubjectsByStudentId(studentId);

        subjectListView = findViewById(R.id.subjectListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);
        subjectListView.setAdapter(adapter);

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = subjects.get(position);

                int subjectId = getSubjectIdByName(selectedSubject);

                Intent intent = new Intent(EtudiantActivity.this, AbsenceDetailsActivity.class);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        });

    }
    public int getSubjectIdByName(String subjectName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + databaseHelper.COL_ID + " FROM " + databaseHelper.TABLE_SUBJECTS +
                " WHERE " + databaseHelper.COL_SUBJECT + " = ?", new String[]{subjectName});
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(databaseHelper.COL_ID);
            if (idColumnIndex >= 0) {
                int subjectId = cursor.getInt(idColumnIndex);
                cursor.close();
                return subjectId;
            }
        }
        cursor.close();
        return -1;
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + databaseHelper.COL_ID + " FROM " + databaseHelper.TABLE_USERS + " WHERE " + databaseHelper.COL_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(databaseHelper.COL_ID);
            if (idColumnIndex >= 0) {
                int userId = cursor.getInt(idColumnIndex);
                cursor.close();
                return userId;
            }
        }
        cursor.close();
        return -1;
    }
    public List<String> getSubjectsByStudentId(int studentId) {
        List<String> subjects = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + databaseHelper.COL_SUBJECT + " FROM " + databaseHelper.TABLE_SUBJECTS +
                " WHERE " + databaseHelper.COL_ID + " IN (SELECT " + databaseHelper.COL_SUBJECT_ID + " FROM " + databaseHelper.TABLE_STUDENT_SUBJECT +
                " WHERE " + databaseHelper.COL_STUDENT_ID + " = ?)", new String[]{String.valueOf(studentId)});
        while (cursor.moveToNext()) {
            int subjectColumnIndex = cursor.getColumnIndex(databaseHelper.COL_SUBJECT);
            if (subjectColumnIndex >= 0) {
                String subject = cursor.getString(subjectColumnIndex);
                subjects.add(subject);
            }
        }
        cursor.close();
        return subjects;
    }
}