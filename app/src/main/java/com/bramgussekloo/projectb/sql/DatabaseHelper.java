package com.bramgussekloo.projectb.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import com.bramgussekloo.projectb.models.DatabaseModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
//http://www.androidtutorialshub.com/android-login-and-register-with-sqlite-database-tutorial/
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "user";
    private static final String DATABASE_NAME = "userManager.db";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_EMAIL + COLUMN_USER_NAME + "TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }
    public void AddUser(DatabaseModel user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public List<DatabaseModel> getAllUser() {
        String[] columns = {
                COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_NAME, COLUMN_USER_PASSWORD
        };
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<DatabaseModel> userlist = new ArrayList<DatabaseModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USER,
                columns,
                null,
                null,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToFirst()){
            do {
                DatabaseModel user = new DatabaseModel();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                userlist.add(user);

            } while(cursor.moveToNext());


        }
        cursor.close();
        db.close();
        return userlist;

    }
    public void updateUser(DatabaseModel user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.update(TABLE_USER, values, COLUMN_USER_ID + "= ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public void deleteUser(DatabaseModel user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, COLUMN_USER_ID + "= ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public Boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + "= ?" + " AND " + COLUMN_USER_PASSWORD + "= ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(
                TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
                );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


}
