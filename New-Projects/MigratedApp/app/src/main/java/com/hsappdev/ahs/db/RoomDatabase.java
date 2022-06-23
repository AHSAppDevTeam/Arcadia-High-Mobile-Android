package com.hsappdev.ahs.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.hsappdev.ahs.newDataTypes.ArticleDataType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ArticleDataType.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static final String TAG = "RoomDatabase";

    public abstract ArticleDAO articleDAO();

    private static volatile RoomDatabase INSTANCE;
    private static final int NUM_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_THREADS);

    static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class,
                            DatabaseConstants.DATABASE_NAME)
                            .fallbackToDestructiveMigrationFrom(1, 2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
