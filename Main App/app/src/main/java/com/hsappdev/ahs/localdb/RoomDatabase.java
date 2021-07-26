package com.hsappdev.ahs.localdb;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.dataTypes.ArticleDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Article.class}, version = 7, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract ArticleDAO articleDAO();

    private static volatile RoomDatabase INSTANCE;
    private static final int NUM_THREADS = 4; // idk, this was in the example
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    static RoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class,
                            Article.TABLE_NAME)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
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
}
