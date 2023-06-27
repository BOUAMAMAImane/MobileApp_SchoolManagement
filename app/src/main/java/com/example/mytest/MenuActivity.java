package com.example.mytest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {
    private Button button1, button2, button3;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");

        button1 = findViewById(R.id.Button1);
        button2 = findViewById(R.id.Button2);
        button3 = findViewById(R.id.Button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ProfessorSubjectsActivity.class);
                int professorId = getProfessorId(userEmail);
                intent.putExtra("professorId", professorId);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AddAbsenceActivity.class);
                int professorId = getProfessorId(userEmail);
                intent.putExtra("professorId", professorId);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ListSubjectActivity.class);
                int professorId = getProfessorId(userEmail);
                intent.putExtra("professorId", professorId);
                startActivity(intent);
            }
        });
    }
    private int getProfessorId(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.COL_ID + " FROM " + DatabaseHelper.TABLE_USERS +
                " WHERE " + DatabaseHelper.COL_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        int professorId = -1;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.COL_ID);
            if (columnIndex != -1) {
                professorId = cursor.getInt(columnIndex);
            }
        }
        cursor.close();
        return professorId;
    }

}