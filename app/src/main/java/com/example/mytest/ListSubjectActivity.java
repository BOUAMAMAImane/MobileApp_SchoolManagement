package com.example.mytest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ListSubjectActivity extends AppCompatActivity {

    private ListView subjectListView;
    private ArrayAdapter<String> subjectAdapter;
    private List<String> subjectList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);

        subjectListView = findViewById(R.id.subjectListView);
        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        int professorId = intent.getIntExtra("professorId", -1);

        subjectList = getSubjectList(professorId);

        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectList);
        subjectListView.setAdapter(subjectAdapter);

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = subjectList.get(position);
                int subjectId = getSubjectId(selectedSubject);

                Intent intent = new Intent( ListSubjectActivity.this, AbsentStudentsActivity.class);
                intent.putExtra("subjectId", subjectId);
                startActivity(intent);
            }
        });
    }

    private List<String> getSubjectList(int professorId) {
        List<String> subjectList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_SUBJECT +
                " FROM " + DatabaseHelper.TABLE_SUBJECTS +
                " WHERE " + DatabaseHelper.COL_PROFESSOR_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(professorId)}); // Remplacez "professorId" par l'ID réel du professeur connecté

        if (cursor.moveToFirst()) {
            do {
                String subject = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_SUBJECT));
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return subjectList;
    }


    private int getSubjectId(String subjectName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_ID + " FROM " + DatabaseHelper.TABLE_SUBJECTS +
                " WHERE " + DatabaseHelper.COL_SUBJECT + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{subjectName});
        int subjectId = -1;

        if (cursor.moveToFirst()) {
            subjectId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
        }

        cursor.close();
        return subjectId;
    }
}