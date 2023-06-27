package com.example.mytest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDetailsActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private String userEmail;
    private int subjectId;
    private ListView absencesListView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absence_details);

        databaseHelper = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("userEmail");
        subjectId = getIntent().getIntExtra("subjectId", -1);

        absencesListView1 = findViewById(R.id.absencesListView1);

        List<String> absencesList = retrieveAbsencesList(userEmail, subjectId);

        ArrayAdapter<String> absencesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, absencesList);
        absencesListView1.setAdapter(absencesAdapter);
    }

    private List<String> retrieveAbsencesList(String email, int subjectId) {
        List<String> absencesList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_SESSION + ", " + DatabaseHelper.COL_SESSION_DATE + ", " +
                DatabaseHelper.COL_PENALTY + ", " + DatabaseHelper.COL_STUDENT_EMAIL + ", " +
                DatabaseHelper.COL_SUBJECT_ID + ", " + DatabaseHelper.COL_JUSTIFICATION +
                " FROM " + DatabaseHelper.TABLE_DETAILS +
                " WHERE " + DatabaseHelper.COL_STUDENT_EMAIL + " = ?" +
                " AND " + DatabaseHelper.COL_SUBJECT_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, String.valueOf(subjectId)});

        int sessionIndex = cursor.getColumnIndex(DatabaseHelper.COL_SESSION);
        int dateIndex = cursor.getColumnIndex(DatabaseHelper.COL_SESSION_DATE);
        int penaltyIndex = cursor.getColumnIndex(DatabaseHelper.COL_PENALTY);
        int studentEmailIndex = cursor.getColumnIndex(DatabaseHelper.COL_STUDENT_EMAIL);
        int justificationIndex = cursor.getColumnIndex(DatabaseHelper.COL_JUSTIFICATION);

        if (cursor.moveToFirst()) {
            do {
                String session = cursor.getString(sessionIndex);
                String date = cursor.getString(dateIndex);
                String penalty = cursor.getString(penaltyIndex);
                String studentEmail = cursor.getString(studentEmailIndex);
                int justification = cursor.getInt(justificationIndex);
                String justificationText= (justification == 0) ? "Non justifié" : "Justifié";

                String absenceItem1 =
                        "Email: " + studentEmail + "\n" +
                                "Session: " + session + "\n" +
                                "Date: " + date + "\n" +
                                "Penalty: " + penalty + "\n" +
                                "Justification: " + justificationText;
                absencesList.add(absenceItem1);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return absencesList;
    }
}