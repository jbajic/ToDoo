package com.jbajic.todoo.helpers;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jbajic.todoo.models.User;

/**
 * Created by jure on 15.05.17..
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "project_management";

    /**
     * Tables
     */
    private static final String TABLE_USER = "user";
    private static final String TABLE_PROJECT = "project";
    private static final String TABLE_TASK = "task";
    private static final String TABLE_PROJECT_TASK= "project_task";
    private static final String TABLE_PROJECT_USER = "project_user";

    private static DatabaseHelper databaseHelper;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null,  DATABASE_VERSION);
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Column names
     */
    //users
    private static final String KEY_ID = "id";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_ME = "is_me";
    private static final String KEY_USER_ID = "user_id";

    //projects
    private static final String KEY_NAME = "name";
    private static final String KEY_BODY = "body";
    private static final String KEY_CLIENT = "client";
    private static final String KEY_DEADLINE = "deadline";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_MANAGER_ID = "manager_id";

    //tasks
    private static final String KEY_ESTIMATED_TIME = "estimated_time";
    private static final String KEY_PROJECT_ID = "project_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_TASK_ID = "task_id";

    /**
     * Table creation queries
     */
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_SERVER_ID + " INTEGER," +
            KEY_EMAIL + " TEXT," +
            KEY_IMAGE_PATH + " TEXT," +
            KEY_USERNAME + " TEXT," +
            KEY_IS_ME + " INTEGER)";

    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE " + TABLE_PROJECT + " (" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_SERVER_ID + " INTEGER," +
            KEY_NAME + " TEXT," +
            KEY_BODY + " TEXT," +
            KEY_CLIENT + " TEXT," +
            KEY_DEADLINE + " TEXT," +
            KEY_COMPLETED + " INTEGER," +
            KEY_MANAGER_ID + " INTEGER)";

    private static final String CREATE_TABLE_TASK = "CREATE TABLE " + TABLE_TASK + " (" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_SERVER_ID + " INTEGER," +
            KEY_NAME + " TEXT," +
            KEY_BODY + " TEXT," +
            KEY_COMPLETED + " INTEGER," +
            KEY_ESTIMATED_TIME + " INTEGER," +
            KEY_CATEGORY_ID + " INTEGER," +
            KEY_PROJECT_ID + " INTEGER)";

    private static final String CREATE_TABLE_PROJECT_USER = "CREATE TABLE " + TABLE_PROJECT_USER + " (" +
            KEY_PROJECT_ID + " INTEGER," +
            KEY_USER_ID + " INTEGER)";

    private static final String CREATE_TABLE_PROJECT_TASK = "CREATE TABLE " + TABLE_PROJECT_TASK + " (" +
            KEY_PROJECT_ID + " INTEGER," +
            KEY_TASK_ID + " INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_PROJECT_USER);
        db.execSQL(CREATE_TABLE_PROJECT_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_TASK);
        onCreate(db);
    }

    /**
     * User queries
     */

    public void addUser(User user) {

    }
}
