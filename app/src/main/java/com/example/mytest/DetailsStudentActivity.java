package com.example.mytest;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DetailsStudentActivity extends AppCompatActivity {
    private ListView absencesListView;
    private String email;
    private int subjectId;
    private int absenceId;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_student);

        databaseHelper = new DatabaseHelper(this);

        email = getIntent().getStringExtra("email");
        subjectId = getIntent().getIntExtra("subjectId", -1);

        absencesListView = findViewById(R.id.absencesListView);

        List<String> absencesList = retrieveAbsencesList(email, subjectId);

        ArrayAdapter<String> absencesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, absencesList);
        absencesListView.setAdapter(absencesAdapter);
        absencesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                int absenceId = extractAbsenceId(selectedItem);

                Intent intent = new Intent(DetailsStudentActivity.this, MenuDetailsActivity.class);
                intent.putExtra("absenceId", absenceId);
                intent.putExtra("email", email);
                intent.putExtra("absenceDetails", selectedItem);
                startActivity(intent);
            }
        });
    }
    private int extractAbsenceId(String selectedItem) {
        int startIndex = selectedItem.indexOf(":") + 1;
        int endIndex = selectedItem.indexOf("\n");

        String absenceIdString = selectedItem.substring(startIndex, endIndex).trim();

        return Integer.parseInt(absenceIdString);
    }

    private List<String> retrieveAbsencesList(String email, int subjectId) {
        List<String> absencesList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_DETAILS_ID + ", " +
                DatabaseHelper.COL_SESSION + ", " + DatabaseHelper.COL_SESSION_DATE + ", " +
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
        int absenceIdIndex = cursor.getColumnIndex(DatabaseHelper.COL_DETAILS_ID);
        if (cursor.moveToFirst()) {
            do {
                String session = cursor.getString(sessionIndex);
                String date = cursor.getString(dateIndex);
                String penalty = cursor.getString(penaltyIndex);
                String studentEmail = cursor.getString(studentEmailIndex);
                int justification = cursor.getInt(justificationIndex);
                String justificationText= (justification == 0) ? "Non justifié" : "Justifié";

                int absenceId = cursor.getInt(absenceIdIndex);
                String absenceItem1 =
                        "ID: " + absenceId + "\n" +
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