package com.hsappdev.ahs.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HistoryDatabase extends SQLiteOpenHelper {

    private static HistoryDatabase dbInstance;


    // singleton
    public static HistoryDatabase getInstance(Context context) {
        if(dbInstance == null)
        {
            dbInstance = new HistoryDatabase(context.getApplicationContext());
        }
        return dbInstance;
    }

    public HistoryDatabase(@Nullable Context context) {
        super(context, DatabaseConstants.SAVED_TABLE, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
