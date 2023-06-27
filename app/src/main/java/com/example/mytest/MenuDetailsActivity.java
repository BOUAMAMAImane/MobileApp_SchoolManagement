package com.example.mytest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MenuDetailsActivity extends AppCompatActivity {
    private int absenceId;
    private String email;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

        databaseHelper = new DatabaseHelper(this);
        absenceId = getIntent().getIntExtra("absenceId", 0);

        Button modifyButton = findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuDetailsActivity.this, ModifyAbsenceActivity.class);
                intent.putExtra("absenceId", absenceId);
                startActivity(intent);
            }
        });

        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = getIntent().getStringExtra("email");

                if (email != null && !email.isEmpty()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + email));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Message body");

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    } else {
                        Toast.makeText(MenuDetailsActivity.this, "No email app available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MenuDetailsActivity.this, "Student email not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button deleteButton = findViewById(R.id.Supprimer);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int absenceId = getIntent().getIntExtra("absenceId", 0);

                boolean success = deleteAbsence(absenceId);

                if (success) {
                    Toast.makeText(MenuDetailsActivity.this, "Absence supprimer", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuDetailsActivity.this, "Echec dees upprimer labsence", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });}

    private boolean deleteAbsence(int absenceId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DETAILS, new String[]{DatabaseHelper.COL_STUDENT_SUBJECT_ID}, DatabaseHelper.COL_DETAILS_ID + " = ?", new String[]{String.valueOf(absenceId)}, null, null, null);
        int studentSubjectId = -1;
        if (cursor.moveToFirst()) {
            studentSubjectId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_STUDENT_SUBJECT_ID));
        }
        cursor.close();

        int rowsAffectedDetails = db.delete(DatabaseHelper.TABLE_DETAILS, DatabaseHelper.COL_DETAILS_ID + " = ?", new String[]{String.valueOf(absenceId)});

        int rowsAffectedStudentSubject = 0;
        if (studentSubjectId != -1) {
            rowsAffectedStudentSubject = db.delete(DatabaseHelper.TABLE_STUDENT_SUBJECT, DatabaseHelper.COL_ID + " = ?", new String[]{String.valueOf(studentSubjectId)});
        }

        db.close();
        return rowsAffectedDetails > 0 && rowsAffectedStudentSubject > 0;
    }



}