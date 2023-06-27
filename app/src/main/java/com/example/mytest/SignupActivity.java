package com.example.mytest;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private RadioGroup userTypeRadioGroup;
    private Button registerButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        registerButton = findViewById(R.id.registerButton);

        databaseHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String userType = getUserTypeFromRadioGroup();

                if (email.isEmpty() || password.isEmpty() || userType.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = addUser(email, userType, password);
                    if (success) {
                        Toast.makeText(SignupActivity.this, "Compte creer avec succees", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Echec de creer un compte", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String getUserTypeFromRadioGroup() {
        int selectedRadioButtonId = userTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.userTypeAdminRadioButton) {
            return "Prof";
        } else if (selectedRadioButtonId == R.id.userTypeRegularRadioButton) {
            return "Etudiant";
        }
        return "";
    }

    private boolean addUser(String email, String userType, String password) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_EMAIL, email);
        values.put(DatabaseHelper.COL_USERTYPE, userType);
        values.put(DatabaseHelper.COL_PASSWORD, password);
        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        return result != -1;
    }
}
