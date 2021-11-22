package com.hsappdev.ahs.localdb;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.ArticleDAO;
import com.hsappdev.ahs.dataTypes.Category;
import com.hsappdev.ahs.dataTypes.CategoryDAO;
import com.hsappdev.ahs.dataTypes.CategoryList;
import com.hsappdev.ahs.dataTypes.CategoryListDAO;
import com.hsappdev.ahs.db.DatabaseConstants;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class, Category.class, CategoryList.class}, version = 7, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static final String TAG = "RoomDatabase";

    public abstract ArticleDAO articleDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract CategoryListDAO categoryListDAO();

    private static volatile RoomDatabase INSTANCE;
    private static final int NUM_THREADS = 4; // idk, this was in the example
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    static RoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class,
                            DatabaseConstants.DATABASE_NAME)
//                            .addCallback(
//                                new Callback(){
//
//                                    @Override
//                                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                                        super.onCreate(db);
//                                        // This code is to copy articles from the old app when the user upgrades
//
//                                        // First open up the old saved article db
//                                        SQLiteDatabase oldDatabase;
//                                        try {
//                                            oldDatabase = SQLiteDatabase.openDatabase("/data/data/com.hsappdev.ahs/databases/" + Article.OLD_TABLE_NAME, null, 0);
//                                        } catch (SQLiteCantOpenDatabaseException e) {
//                                            // the db does not exist
//                                            // do nothing
//                                            return;
//                                        }
//                                        Cursor cursor = oldDatabase.rawQuery("select * from " + Article.OLD_TABLE_NAME,null);
//
//                                        // Cycle through all entries and add them to the new db
//                                        db.beginTransaction();
//                                        int num = 0;
//                                        if (cursor.moveToFirst()) {
//                                            while (!cursor.isAfterLast()) {
//                                                num++;
//
//
//                                                int ID_INDEX = cursor.getColumnIndex("IDS");
//                                                int TIME_INDEX = cursor.getColumnIndex("TIME");
//                                                int TITLE_INDEX = cursor.getColumnIndex("TITLE");
//                                                int AUTHOR_INDEX = cursor.getColumnIndex("AUTHOR");
//                                                int STORY_INDEX = cursor.getColumnIndex("STORY");
//                                                int IPATHS_INDEX = cursor.getColumnIndex("IPATHS");
//                                                int V_IDS_INDEX = cursor.getColumnIndex("V_IDS");
//                                                int CATEGORY_INDEX = cursor.getColumnIndex("CATEGORY");
//
//
//
//
//                                                Log.d(TAG, "onCreate: I FOUND AN ARTICLE IN THE OLD DATABASE " + cursor.getString(ID_INDEX) + " : " + cursor.getString(TITLE_INDEX));
//
//                                                // TODO: add the article to the new db
//                                                ContentValues values = new ContentValues();
//                                                values.put("ID", num);
//                                                values.put(Article.ID, cursor.getString(ID_INDEX));
//                                                values.put(Article.AUTHOR, cursor.getString(AUTHOR_INDEX));
//                                                values.put(Article.TITLE, cursor.getString(TITLE_INDEX));
//                                                values.put(Article.BODY, cursor.getString(STORY_INDEX));
//                                                values.put(Article.IMG_URLS, cursor.getString(IPATHS_INDEX));
//                                                values.put(Article.VID_URLS, cursor.getString(V_IDS_INDEX));
//                                                values.put(Article.CAT_DISP_CLR, Color.BLACK);
//                                                values.put(Article.IS_SAVED, 1); // SQL lite does not support boolean
//                                                values.put(Article.IS_NOTIFICATION, 0);
//                                                values.put(Article.IS_VIEWED, 0);
//                                                values.put(Article.CAT_DISP, "General");
//                                                values.put(Article.CAT_ID, "General_Info");
//                                                values.put(Article.TIME, cursor.getLong(TIME_INDEX));
//
//                                                try {
//                                                    long result = db.insert(Article.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values);
//                                                    boolean succeeded = (result != -1);
//                                                    Log.d(TAG, "I FOUND AN ARTICLE IN THE OLD DATABASE AND ADDED IT, DID IT SUCCEED? : " + succeeded);
//                                                } catch (SQLException e) {
//                                                    Log.e(TAG, "onCreate: ", e);
//                                                }
//
//
//                                            }
//                                        }
//                                        db.endTransaction();
//                                        cursor.close();
//                                        oldDatabase.close();
//
//
//                                    }
//                                }
//                            )
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .addMigrations(MIGRATION_7_8)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final String table_name_placeholder = "old_saved";
            database.execSQL("ALTER TABLE " + Article.TABLE_NAME + " RENAME TO " + table_name_placeholder);
            database.execSQL("CREATE TABLE " + Article.TABLE_NAME +
                    "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    Article.ID + " TEXT," +
                    Article.AUTHOR + " TEXT," +
                    Article.TITLE + " TEXT," +
                    Article.BODY + " TEXT," +
                    Article.CAT_ID + " TEXT," +
                    Article.IMG_URLS + " TEXT," +
                    Article.VID_URLS + " TEXT," +
                    Article.TIME + " INTEGER NOT NULL DEFAULT 0," +
                    Article.CAT_DISP + " TEXT," +
                    Article.CAT_DISP_CLR + " INTEGER NOT NULL DEFAULT 0," +
                    Article.IS_SAVED +  " INTEGER NOT NULL DEFAULT 1, " + // Default to true
                    Article.IS_NOTIFICATION +  " INTEGER NOT NULL DEFAULT 0" + ");" // Default to false
            );
            database.execSQL("INSERT INTO " + Article.TABLE_NAME +
                    "("+
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR + ")" +
                    "SELECT " +
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR +
                    " FROM "  + table_name_placeholder + ";");
            database.execSQL("DROP TABLE "  + table_name_placeholder);
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final String table_name_placeholder = "old_saved";
            database.execSQL("ALTER TABLE " + Article.TABLE_NAME + " RENAME TO " + table_name_placeholder);
            database.execSQL("CREATE TABLE " + Article.TABLE_NAME +
                    "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    Article.ID + " TEXT," +
                    Article.AUTHOR + " TEXT," +
                    Article.TITLE + " TEXT," +
                    Article.BODY + " TEXT," +
                    Article.CAT_ID + " TEXT," +
                    Article.IMG_URLS + " TEXT," +
                    Article.VID_URLS + " TEXT," +
                    Article.TIME + " INTEGER NOT NULL DEFAULT 0," +
                    Article.CAT_DISP + " TEXT," +
                    Article.CAT_DISP_CLR + " INTEGER NOT NULL DEFAULT 0," +
                    Article.IS_SAVED +  " INTEGER NOT NULL DEFAULT 1, " + // Default to true
                    Article.IS_NOTIFICATION +  " INTEGER NOT NULL DEFAULT 0" + // Default to false
                    ");"
            );
            database.execSQL("INSERT INTO " + Article.TABLE_NAME +
                    "("+
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR + ") " +
                    "SELECT " +
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR +
                    " FROM "  + table_name_placeholder + ";");
            database.execSQL("DROP TABLE "  + table_name_placeholder);
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final String table_name_placeholder = "old_saved";
            database.execSQL("ALTER TABLE " + Article.TABLE_NAME + " RENAME TO " + table_name_placeholder);
            database.execSQL("CREATE TABLE " + Article.TABLE_NAME +
                    "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    Article.ID + " TEXT," +
                    Article.AUTHOR + " TEXT," +
                    Article.TITLE + " TEXT," +
                    Article.BODY + " TEXT," +
                    Article.CAT_ID + " TEXT," +
                    Article.IMG_URLS + " TEXT," +
                    Article.VID_URLS + " TEXT," +
                    Article.TIME + " INTEGER NOT NULL DEFAULT 0," +
                    Article.CAT_DISP + " TEXT," +
                    Article.CAT_DISP_CLR + " INTEGER NOT NULL DEFAULT 0," +
                    Article.IS_SAVED +  " INTEGER NOT NULL DEFAULT 1, " + // Default to true
                    Article.IS_NOTIFICATION +  " INTEGER NOT NULL DEFAULT 0," + // Default to false
                    Article.IS_VIEWED +  " INTEGER NOT NULL DEFAULT 0" + // Default to false
                    ");"
            );
            database.execSQL("INSERT INTO " + Article.TABLE_NAME +
                    "("+
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR + ") " +
                    "SELECT " +
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR +
                    " FROM "  + table_name_placeholder + ";");
            database.execSQL("DROP TABLE "  + table_name_placeholder);
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final String table_name_placeholder = "old_saved";
            database.execSQL("ALTER TABLE " + Article.TABLE_NAME + " RENAME TO " + table_name_placeholder);
            database.execSQL("CREATE TABLE " + Article.TABLE_NAME +
                    "(" +
                    Article.ID + " TEXT PRIMARY KEY," +
                    Article.AUTHOR + " TEXT," +
                    Article.TITLE + " TEXT," +
                    Article.BODY + " TEXT," +
                    Article.CAT_ID + " TEXT," +
                    Article.IMG_URLS + " TEXT," +
                    Article.VID_URLS + " TEXT," +
                    Article.TIME + " INTEGER NOT NULL DEFAULT 0," +
                    Article.CAT_DISP + " TEXT," +
                    Article.CAT_DISP_CLR + " INTEGER NOT NULL DEFAULT 0," +
                    Article.IS_SAVED +  " INTEGER NOT NULL DEFAULT 1, " + // Default to true
                    Article.IS_NOTIFICATION +  " INTEGER NOT NULL DEFAULT 0" + // Default to false
                    ");"
            );
            database.execSQL("INSERT INTO " + Article.TABLE_NAME +
                    "("+
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR + ") " +
                    "SELECT " +
                    Article.ID + ", " +
                    Article.AUTHOR + ", " +
                    Article.TITLE + ", " +
                    Article.BODY + ", " +
                    Article.CAT_ID + ", " +
                    Article.IMG_URLS + ", " +
                    Article.VID_URLS + ", " +
                    Article.TIME + ", " +
                    Article.CAT_DISP + ", " +
                    Article.CAT_DISP_CLR +
                    " FROM "  + table_name_placeholder + ";");
            database.execSQL("DROP TABLE "  + table_name_placeholder);
        }
    };

    /**
     * CREATION OF CATEGORY LOCAL CACHE
     */
    static final Migration MIGRATION_6_7 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE " + Category.TABLE_NAME +
                    "(" +
                    Category.ID + " TEXT PRIMARY KEY," +
                    Category.TITLE + " TEXT," +
                    Category.IMG_URL + " TEXT," +
                    Category.ARTICLE_IDS + " TEXT," +
                    Category.CLR + " INTEGER NOT NULL DEFAULT 0" +
                    ");"
            );
        }
    };

    /**
     * CREATION OF CATEGORY LIST LOCAL CACHE
     */
    static final Migration MIGRATION_7_8 = new Migration(7,8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE " + CategoryList.TABLE_NAME +
                    "(" +
                    CategoryList.ID + " TEXT PRIMARY KEY," +
                    CategoryList.CATEGORY_IDS + " TEXT" +
                    ");"
            );
        }
    };
}
