package com.example.mytest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        databaseHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                User user = getUserByEmail(email);
                if (user != null && user.getPassword().equals(password)) {
                    if (user.getUserType().equals("Prof")) {
                        redirectToProfActivity();
                    } else if (user.getUserType().equals("Etudiant")) {
                        redirectToEtudiantActivity();
                    }
                } else {

                    Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private User getUserByEmail(String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.COL_EMAIL + " = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(DatabaseHelper.COL_ID);
            int userTypeColumnIndex = cursor.getColumnIndex(DatabaseHelper.COL_USERTYPE);
            int passwordColumnIndex = cursor.getColumnIndex(DatabaseHelper.COL_PASSWORD);

            if (idColumnIndex >= 0 && userTypeColumnIndex >= 0 && passwordColumnIndex >= 0) {
                int userId = cursor.getInt(idColumnIndex);
                String userType = cursor.getString(userTypeColumnIndex);
                String password = cursor.getString(passwordColumnIndex);
                User user = new User(email, userType, password);
                user.setId(userId);
                cursor.close();
                return user;
            }
        }
        cursor.close();
        return null;
    }

    private void redirectToProfActivity() {
        String userEmail = emailEditText.getText().toString();
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
        finish();
    }

    private void redirectToEtudiantActivity() {
        String userEmail = emailEditText.getText().toString();
        Intent intent = new Intent(LoginActivity.this,EtudiantActivity.class);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
        finish();
    }


}
