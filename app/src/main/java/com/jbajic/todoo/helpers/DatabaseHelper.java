package com.jbajic.todoo.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jbajic.todoo.models.Project;
import com.jbajic.todoo.models.Task;
import com.jbajic.todoo.models.User;

import java.util.ArrayList;
import java.util.List;

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
    private static final String TABLE_USER_TASK = "user_task";
    private static final String TABLE_PROJECT_USER = "project_user";

    private static DatabaseHelper databaseHelper;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
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
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
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
            KEY_USERNAME + " TEXT," +
            KEY_FIRST_NAME + " TEXT," +
            KEY_LAST_NAME + " TEXT," +
            KEY_ADDRESS + " TEXT," +
            KEY_CITY + " TEXT," +
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

    private static final String CREATE_TABLE_TASK_USER = "CREATE TABLE " + TABLE_USER_TASK + " (" +
            KEY_USER_ID + " INTEGER," +
            KEY_TASK_ID + " INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_PROJECT_USER);
        db.execSQL(CREATE_TABLE_TASK_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_TASK);
        onCreate(db);
    }

    /**
     * User queries
     */

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                userList.add(new User(
                        Long.valueOf(cursor.getString(0)),
                        Long.valueOf(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getInt(8) > 0
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return userList;
    }

    public Cursor getAllUsersCursor() {
        // Select All Query
        String selectQuery = "SELECT id AS _id, first_name || ' ' ||  FROM last_name AS name " + TABLE_USER;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return cursor;
        } else {
            return null;
        }
    }

    public void addUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, user.getServerId());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_FIRST_NAME, user.getfName());
        values.put(KEY_LAST_NAME, user.getlName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_CITY, user.getCity());
        values.put(KEY_IS_ME, user.getMe());

        sqLiteDatabase.insert(TABLE_USER, null, values);
        sqLiteDatabase.close();
    }

    public void updateUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, user.getServerId());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_FIRST_NAME, user.getfName());
        values.put(KEY_LAST_NAME, user.getlName());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_CITY, user.getCity());
        values.put(KEY_IS_ME, user.getMe());

        sqLiteDatabase.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        sqLiteDatabase.close();
    }

    public void deleteUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_USER, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        sqLiteDatabase.close();
    }

    public void deleteUsers() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_USER, null, null);
        sqLiteDatabase.close();
    }

    /**
     * Project queries
     */

    public List<Project> getAllProjects() {
        List<Project> projectList = new ArrayList<Project>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROJECT;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(
                        Long.valueOf(cursor.getString(0)),
                        Long.valueOf(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getInt(6) > 0,
                        Long.valueOf(cursor.getString(7)));
                project.setTaskArrayList((ArrayList<Task>) getProjectTasks(project.getId()));

                projectList.add(project);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return projectList;
    }

    public Long addProject(Project project) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, project.getServerId());
        values.put(KEY_NAME, project.getName());
        values.put(KEY_BODY, project.getBody());
        values.put(KEY_CLIENT, project.getClient());
        values.put(KEY_DEADLINE, project.getDeadline());
        values.put(KEY_COMPLETED, project.getCompleted());
        values.put(KEY_MANAGER_ID, project.getManagerId());

        Long projectId = sqLiteDatabase.insert(TABLE_PROJECT, null, values);
        sqLiteDatabase.close();
        return projectId;
    }

    public void updateProject(Project project) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, project.getServerId());
        values.put(KEY_NAME, project.getName());
        values.put(KEY_BODY, project.getBody());
        values.put(KEY_CLIENT, project.getClient());
        values.put(KEY_DEADLINE, project.getDeadline());
        values.put(KEY_COMPLETED, project.getCompleted());
        values.put(KEY_MANAGER_ID, project.getManagerId());

        sqLiteDatabase.update(TABLE_PROJECT, values, KEY_ID + " = ?",
                new String[]{String.valueOf(project.getId())});
        sqLiteDatabase.close();
    }

    public void deleteProject(Project project) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PROJECT, KEY_ID + " = ?",
                new String[]{String.valueOf(project.getId())});
        sqLiteDatabase.close();
    }

    public void deleteProjects() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PROJECT, null, null);
        sqLiteDatabase.close();
    }

    /**
     * Task queries
     */

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASK;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                taskList.add(new Task(
                        Long.valueOf(cursor.getString(0)),
                        Long.valueOf(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) > 0,
                        Integer.valueOf(cursor.getString(5)),
                        Long.valueOf(cursor.getString(6)),
                        Long.valueOf(cursor.getString(7))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return taskList;
    }

    public List<Task> getProjectTasks(Long projectId) {
        List<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASK +
                " WHERE " + KEY_PROJECT_ID + "=" + projectId;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                taskList.add(new Task(
                        Long.valueOf(cursor.getString(0)),
                        Long.valueOf(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4) > 0,
                        Integer.valueOf(cursor.getString(5)),
                        cursor.getLong(6),
                        cursor.getLong(7)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return taskList;
    }

    public void addTask(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, task.getServerId());
        values.put(KEY_NAME, task.getName());
        values.put(KEY_BODY, task.getBody());
        values.put(KEY_COMPLETED, task.getCompleted());
        values.put(KEY_ESTIMATED_TIME, task.getEstimatedTime());
        values.put(KEY_CATEGORY_ID, task.getCategoryId());
        values.put(KEY_PROJECT_ID, task.getProjectId());

        sqLiteDatabase.insert(TABLE_TASK, null, values);
        sqLiteDatabase.close();
    }

    public void updateTask(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_ID, task.getServerId());
        values.put(KEY_NAME, task.getName());
        values.put(KEY_BODY, task.getBody());
        values.put(KEY_COMPLETED, task.getCompleted());
        values.put(KEY_ESTIMATED_TIME, task.getEstimatedTime());
        values.put(KEY_CATEGORY_ID, task.getCategoryId());
        values.put(KEY_PROJECT_ID, task.getProjectId());

        sqLiteDatabase.update(TABLE_TASK, values, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
    }

    public void deleteTask(Task task) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TASK, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
    }

    public void deleteTasks() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_TASK, null, null);
        sqLiteDatabase.close();
    }

    /**
     * TaskUserQuery
     */
    public void deleteTaskUser() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_USER_TASK, null, null);
        sqLiteDatabase.close();
    }

    /**
     * UserProjectQueries
     */

    public void deleteUserProject() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PROJECT_USER, null, null);
        sqLiteDatabase.close();
    }

//    public void associateTaskUser(User )

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
