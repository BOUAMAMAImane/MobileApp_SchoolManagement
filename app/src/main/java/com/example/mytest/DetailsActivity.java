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

public class DetailsActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private long absenceId;
    private EditText sessionEditText;
    private EditText sessionDateEditText;
    private Spinner penaltySpinner;
    private ArrayList<String> selectedStudents;
    private RadioGroup justificationRadioGroup;
    private int subjectId;
    private String studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        databaseHelper = new DatabaseHelper(this);
        subjectId = getIntent().getIntExtra("subjectId", -1);
        absenceId = getIntent().getLongExtra("absenceId", -1);
        this.selectedStudents = getIntent().getStringArrayListExtra("selectedStudents");
        if (!this.selectedStudents.isEmpty()) {
            studentEmail = this.selectedStudents.get(0);
            subjectId = getIntent().getIntExtra("subjectId", -1);
        }
        if (absenceId != -1) {
            sessionEditText = findViewById(R.id.sessionEditText);
            sessionDateEditText = findViewById(R.id.sessionDateEditText);
            justificationRadioGroup = findViewById(R.id.justificationRadioGroup);
            penaltySpinner = findViewById(R.id.penaltySpinner);

            List<String> penaltyOptions = new ArrayList<>();
            penaltyOptions.add("Reduction de note");
            penaltyOptions.add(" Exclusion temporaire");
            penaltyOptions.add("Conseil de discipline");

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, penaltyOptions);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            penaltySpinner.setAdapter(spinnerAdapter);

            Button saveButton = findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean success = insertDetails(absenceId);

                    if (success) {
                        Toast.makeText(DetailsActivity.this, "Details added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, "Failed to add details", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Invalid absence ID", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean insertDetails(long absenceId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean success = false;

        String session = sessionEditText.getText().toString();
        String sessionDate = sessionDateEditText.getText().toString();
        String penalty = penaltySpinner.getSelectedItem().toString();
        int justificationId = justificationRadioGroup.getCheckedRadioButtonId();
        boolean justification = (justificationId == R.id.justifiedRadioButton);

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_STUDENT_EMAIL, studentEmail);
        values.put(DatabaseHelper.COL_SUBJECT_ID, subjectId);
        values.put(DatabaseHelper.COL_STUDENT_SUBJECT_ID, absenceId);
        values.put(DatabaseHelper.COL_SESSION, session);
        values.put(DatabaseHelper.COL_SESSION_DATE, sessionDate);
        values.put(DatabaseHelper.COL_PENALTY, penalty);
        values.put(DatabaseHelper.COL_JUSTIFICATION, justification);

        long rowId = db.insert(DatabaseHelper.TABLE_DETAILS, null, values);

        if (rowId != -1) {
            success = true;
        }

        return success;
    }
}


