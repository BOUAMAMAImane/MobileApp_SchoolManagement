package com.example.mytest;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Admin.db";
    public static final String TABLE_USERS = "users";

    public static final String COL_ID = "id";

    public static final String COL_EMAIL = "email";
    public static final String COL_USERTYPE = "usertype";
    public static final String COL_PASSWORD = "password";
    public static final String TABLE_SUBJECTS = "subjects";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_PROFESSOR_ID = "professor_id";
    public static final String COL_SUBJECT_ID = "subject_id";
    public static final String TABLE_STUDENT_SUBJECT = "student_subject";
    public static final String COL_STUDENT_ID = "student_id";
    public static final String COL_ABSENT = "absent";
    public static final String COL_SESSION = "session"; // Nouvelle colonne pour la séance
    public static final String COL_SESSION_DATE = "session_date";
    public static final String COL_JUSTIFICATION = "justification"; // Nouvelle colonne pour la justification
    public static final String COL_CNE = "cne"; // Nouvelle colonne pour le CNE
    public static final String COL_PENALTY = "penalty";
    public static final String COL_STATE = "state";
    public static final String TABLE_DETAILS = "details";
    public static final String COL_DETAILS_ID = "details_id";

    public static final String  COL_STUDENT_EMAIL = "student_email";
    public static final String COL_STUDENT_SUBJECT_ID = "student_subject_id";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT, " +
                COL_USERTYPE + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTable);
        String createSubjectTable = "CREATE TABLE " + TABLE_SUBJECTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUBJECT + " TEXT, " +
                COL_PROFESSOR_ID + " INTEGER, " +
                COL_SUBJECT_ID + " INTEGER)";
        db.execSQL(createSubjectTable);

        String createStudentSubjectTable = "CREATE TABLE " + TABLE_STUDENT_SUBJECT + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUBJECT_ID + " INTEGER, " +
                COL_STUDENT_ID + " INTEGER, " +
                COL_ABSENT + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + COL_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COL_ID + "), " +
                "FOREIGN KEY (" + COL_STUDENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "))";
        db.execSQL(createStudentSubjectTable);

        String createDetailsTable = "CREATE TABLE " + TABLE_DETAILS + " (" +
                COL_DETAILS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STUDENT_SUBJECT_ID + " INTEGER, " +
                COL_SESSION + " TEXT, " +
                COL_SESSION_DATE + " TEXT, " +
                COL_JUSTIFICATION + " INTEGER DEFAULT 0, " +
                COL_PENALTY + " TEXT, " +
                COL_STUDENT_EMAIL + " TEXT, " +
                COL_SUBJECT_ID + " INTEGER, " +
                "FOREIGN KEY (" + COL_STUDENT_SUBJECT_ID + ") REFERENCES " + TABLE_STUDENT_SUBJECT + "(" + COL_ID + "))";
        db.execSQL(createDetailsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("CREATE TABLE users_backup AS SELECT * FROM " + TABLE_USERS);
//        db.execSQL("CREATE TABLE subjects_backup AS SELECT * FROM " + TABLE_SUBJECTS);
//        db.execSQL("CREATE TABLE student_subject_backup AS SELECT * FROM " + TABLE_STUDENT_SUBJECT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_SUBJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        // Recréez les tables avec la nouvelle structure
        onCreate(db);


       db.execSQL("INSERT INTO " + TABLE_USERS + " SELECT * FROM users_backup");
//        db.execSQL("INSERT INTO " + TABLE_SUBJECTS + " SELECT * FROM subjects_backup");
//        db.execSQL("INSERT INTO " + TABLE_STUDENT_SUBJECT + " SELECT * FROM student_subject_backup");


        db.execSQL("DROP TABLE IF EXISTS users_backup");
//        db.execSQL("DROP TABLE IF EXISTS subjects_backup");
//        db.execSQL("DROP TABLE IF EXISTS student_subject_backup");
    }

}
