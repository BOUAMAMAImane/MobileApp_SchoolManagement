package com.example.mytest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ModifyAbsenceActivity extends AppCompatActivity {
    private EditText sessionEditText;
    private EditText dateEditText;
    RadioButton radioButtonJustified;
    RadioButton radioButtonNotJustified;
    Spinner penaltySpinner;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_absence);

        sessionEditText = findViewById(R.id.sessionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        radioButtonJustified = findViewById(R.id.radioButtonJustified);
        radioButtonNotJustified = findViewById(R.id.radioButtonNotJustified);
        databaseHelper = new DatabaseHelper(this);

        penaltySpinner = findViewById(R.id.penaltySpinner);

        List<String> penaltyOptions = new ArrayList<>();
        penaltyOptions.add("Reduction de note");
        penaltyOptions.add("Exclusion temporaire");
        penaltyOptions.add("Conseil de discipline");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, penaltyOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        penaltySpinner.setAdapter(spinnerAdapter);

        Button modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSession = sessionEditText.getText().toString();
                String newDate = dateEditText.getText().toString();
                String newPenalty = penaltySpinner.getSelectedItem().toString();
                int newJustification = radioButtonJustified.isChecked() ? 1 : 0;
                int absenceId = getIntent().getIntExtra("absenceId", 0);

                boolean success = updateAbsenceDetails(absenceId, newSession, newDate, newPenalty, newJustification);
                if (success) {

                    Toast.makeText(ModifyAbsenceActivity.this, "details modifier", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ModifyAbsenceActivity.this, "Echec de modifier", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }

    public boolean updateAbsenceDetails(int absenceId, String newSession, String newDate, String newPenalty, int newJustification) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_SESSION, newSession);
        values.put(DatabaseHelper.COL_SESSION_DATE, newDate);
        values.put(DatabaseHelper.COL_PENALTY, newPenalty);
        values.put(DatabaseHelper.COL_JUSTIFICATION, newJustification);

        int rowsAffected = db.update(DatabaseHelper.TABLE_DETAILS, values, DatabaseHelper.COL_DETAILS_ID + " = ?", new String[]{String.valueOf(absenceId)});

        db.close();

        return rowsAffected > 0;
    }
}