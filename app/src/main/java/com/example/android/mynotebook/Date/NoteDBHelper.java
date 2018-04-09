package com.example.android.mynotebook.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Denys_d on 3/31/2018.
 */

public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int VERSION = 1;

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + NoteContract.NoteEntry.TABLE_NAME + " (" +
                NoteContract.NoteEntry._ID + " INTEGER PRIMARY KEY, " +
                NoteContract.NoteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                NoteContract.NoteEntry.COLUMN_PRIOR + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteContract.NoteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
