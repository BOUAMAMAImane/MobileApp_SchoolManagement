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

public class MatiereActivity extends AppCompatActivity {

    private ListView subjectListView;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matiere);

        subjectListView = findViewById(R.id.subjectListView);
        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.getString("userEmail");
        }

        List<String> subjects = getSubjects(userEmail);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);
        subjectListView.setAdapter(adapter);

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = subjects.get(position);

                Intent intent = new Intent();
                intent.putExtra("selectedSubject", selectedSubject);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private List<String> getSubjects(String userEmail) {
        List<String> subjects = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COL_SUBJECT + "." + DatabaseHelper.COL_SUBJECT +
                " FROM " + DatabaseHelper.TABLE_SUBJECTS +
                " INNER JOIN " + DatabaseHelper.TABLE_STUDENT_SUBJECT +
                " ON " + DatabaseHelper.TABLE_SUBJECTS + "." + DatabaseHelper.COL_ID +
                " = " + DatabaseHelper.TABLE_STUDENT_SUBJECT + "." + DatabaseHelper.COL_SUBJECT_ID +
                " WHERE " + DatabaseHelper.TABLE_STUDENT_SUBJECT + "." + DatabaseHelper.COL_STUDENT_ID +
                " = ? AND " + DatabaseHelper.TABLE_STUDENT_SUBJECT + "." + DatabaseHelper.COL_ABSENT + " = 1", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int subjectIndex = cursor.getColumnIndex(DatabaseHelper.COL_SUBJECT);
                String subject = cursor.getString(subjectIndex);
                subjects.add(subject);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return subjects;
    }
}