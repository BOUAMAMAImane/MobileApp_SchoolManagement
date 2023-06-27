package com.example.mytest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AbsentStudentsActivity extends AppCompatActivity {
    private ListView absentStudentsListView;
    private ArrayAdapter<String> absentStudentsAdapter;
    private List<String> absentStudentsList;
    private DatabaseHelper databaseHelper;
    private int subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_students);

        absentStudentsListView = findViewById(R.id.absentStudentsListView);
        databaseHelper = new DatabaseHelper(this);

        subjectId = getIntent().getIntExtra("subjectId", -1);

        absentStudentsList = getAbsentStudentsList(subjectId);

        absentStudentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, absentStudentsList);
        absentStudentsListView.setAdapter(absentStudentsAdapter);

        absentStudentsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = absentStudentsList.get(position);
            int colonIndex = selectedItem.indexOf(":");
            if (colonIndex != -1) {
                String email = selectedItem.substring(0, colonIndex).trim();
                String absenceCountStr = selectedItem.substring(colonIndex + 1).trim();
                int absenceCount = Integer.parseInt(absenceCountStr);

                Toast.makeText(AbsentStudentsActivity.this, "Étudiant : " + email + ", Absences : " + absenceCount, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AbsentStudentsActivity.this, DetailsStudentActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("subjectId", subjectId);
                startActivity(intent);
            }
        });


    }
    private List<String> getAbsentStudentsList(int subjectId) {
        List<String> absentStudentsList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT " + DatabaseHelper.COL_EMAIL + ", COUNT(" + DatabaseHelper.COL_ABSENT + ") AS AbsenceCount" +
                " FROM " + DatabaseHelper.TABLE_USERS +
                " INNER JOIN " + DatabaseHelper.TABLE_STUDENT_SUBJECT +
                " ON " + DatabaseHelper.TABLE_USERS + "." + DatabaseHelper.COL_ID + " = " + DatabaseHelper.TABLE_STUDENT_SUBJECT + "." + DatabaseHelper.COL_STUDENT_ID +
                " AND " + DatabaseHelper.TABLE_STUDENT_SUBJECT + "." + DatabaseHelper.COL_SUBJECT_ID + " = ?" +
                " WHERE " + DatabaseHelper.TABLE_STUDENT_SUBJECT + "." + DatabaseHelper.COL_ABSENT + " = 1" +
                " GROUP BY " + DatabaseHelper.COL_EMAIL;


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(subjectId)});
        if (cursor.moveToFirst()) {
            int emailColumnIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL);
            int absenceCountColumnIndex = cursor.getColumnIndexOrThrow("AbsenceCount");

            do {
                String email = cursor.getString(emailColumnIndex);
                int absenceCount = cursor.getInt(absenceCountColumnIndex);
                String item = email + ": " + absenceCount;
                absentStudentsList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return absentStudentsList;
    }


}