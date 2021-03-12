package project.farmvisor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by admin on 24/02/2017.
 */

public class DBHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "LanguageDatabase";


    private static final String TABLE_LANGUAGE = "language";

    private static final String USER_LANGUAGE="sel_language";
    private static final String USER_ID="sel_id";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LANGUAGE_TABLE="CREATE TABLE " + TABLE_LANGUAGE + "("+ USER_ID + " INTEGER PRIMARY KEY, " + USER_LANGUAGE+" TEXT " +")";
        db.execSQL(CREATE_LANGUAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE);
        // Create tables again
        onCreate(db);
    }

    //////////////////////////////////////////////////////////////////////

    //ADDING USER DATABASE TO TABLE
    public boolean addLanguage(String language) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(USER_ID, 1);
            values.put(USER_LANGUAGE,language );
            // Inserting Row
            db.insert(TABLE_LANGUAGE, null, values);
            db.close(); // Closing database connection

            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    public String  getLanguage(int id)throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.query(TABLE_LANGUAGE, new String[]{USER_ID,
                            USER_LANGUAGE}, USER_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            LanguageObject users = new LanguageObject(cursor.getInt(0),
                    cursor.getString(1));
            // return breed
            return users.getLanguage();
        }catch (Exception e)
        {
            return "";
        }
    }

    public Boolean update(String language)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(USER_ID, 1);
            values.put(USER_LANGUAGE, language);

            db.update(TABLE_LANGUAGE, values, USER_ID + " = "+1
                    ,null);
            db.close();
            return true;
        }catch (Exception e)
        {
            return false;
        }

    }



}
