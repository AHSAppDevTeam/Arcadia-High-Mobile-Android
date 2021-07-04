package com.hsappdev.ahs.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hsappdev.ahs.dataTypes.Article;
import com.hsappdev.ahs.db.DatabaseConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class SavedDatabase extends SQLiteOpenHelper {

    private static SavedDatabase dbInstance;
    // db schema
    public static final String ID = "IDS";
    public static final int ID_COL = 1;
    public static final String AUTHOR = "AUTHOR";
    public static final int AUTHOR_COL = 2;
    public static final String TITLE = "TITLE";
    public static final int TITLE_COL = 3;
    public static final String BODY = "BODY";
    public static final int BODY_COL = 4;
    public static final String CAT_ID = "CAT_ID";
    public static final int CAT_ID_COL = 5;
    public static final String IMG_URLS = "IMG_URLS";
    public static final int IMG_URLS_COL = 6;
    public static final String VID_URLS = "VID_URLS";
    public static final int VID_URLS_COL = 7;
    public static final String TIME = "TIME";
    public static final int TIME_COL = 8;
    public static final String CAT_DISP = "CAT_DISP";
    public static final int CAT_DISP_COL = 9;
    public static final String CAT_DISP_CLR = "CAT_DISP_CLR";
    public static final int CAT_DISP_CLR_COL = 10;

    // singleton
    public static SavedDatabase getInstance(Context context) {
        if(dbInstance == null)
        {
            dbInstance = new SavedDatabase(context.getApplicationContext());
        }
        return dbInstance;
    }

    public SavedDatabase(@Nullable Context context) {
        super(context, DatabaseConstants.SAVED_TABLE, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + DatabaseConstants.SAVED_TABLE +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ID + " TEXT," +
                        AUTHOR + " TEXT," +
                        TITLE + " TEXT," +
                        BODY + " TEXT," +
                        CAT_ID + " TEXT," +
                        IMG_URLS + " TEXT," +
                        VID_URLS + " TEXT," +
                        TIME + " TEXT," +
                        CAT_DISP + " TEXT," +
                        CAT_DISP_CLR + " INTEGER);"
                ;
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.SAVED_TABLE);
        onCreate(db);
    }

    public void deleteAll() {
        this.getWritableDatabase().delete(DatabaseConstants.SAVED_TABLE, null, null);
    }

    public boolean add(Article... articles) {
        boolean succeeded = true;

        for(Article article: articles) {

            ContentValues values = new ContentValues();
            values.put(ID, article.getArticleID());
            values.put(AUTHOR, article.getAuthor());
            values.put(TITLE, article.getTitle());
            values.put(BODY, article.getBody());
            values.put(CAT_ID, article.getCategoryID());
            values.put(IMG_URLS, convertArrayToString(article.getImageURLs()));
            values.put(VID_URLS, convertArrayToString(article.getVideoURLs()));
            values.put(TIME, article.getTimestamp());
            values.put(CAT_DISP, article.getCategoryDisplayName());
            values.put(CAT_DISP_CLR, article.getCategoryDisplayColor());


            long result = this.getWritableDatabase().insert(DatabaseConstants.SAVED_TABLE, null, values);
            succeeded = succeeded && (result != -1);
        }

        // if inserted incorrectly -1 is the return value
        return succeeded;
    }

    public boolean articleIsSaved(Article article){
        String selectQuery =
                "SELECT "+ ID +
                " FROM " + DatabaseConstants.SAVED_TABLE +
                " WHERE  " + ID + " = '" + article.getArticleID() + "'";
        Cursor cursor = this.getWritableDatabase().rawQuery(selectQuery, null);
        boolean isSaved = cursor.getCount() > 0;
        cursor.close();

        return isSaved;
    }

    /**
     * Helper method to get a single article and perform a callback, to avoid duplicate code.
     * @param callback
     * @param data
     */
    private void getSingleSavedArticle(ArticleLoadedCallback callback, Cursor data){
        callback.onArticleLoaded(
                new Article(
                        data.getString(ID_COL),
                        data.getString(AUTHOR_COL),
                        data.getString(TITLE_COL),
                        data.getString(BODY_COL),
                        data.getString(CAT_ID_COL),
                        convertStringToArray(data.getString(IMG_URLS_COL)),
                        convertStringToArray(data.getString(VID_URLS_COL)),
                        data.getLong(TIME_COL),
                        data.getString(CAT_DISP_COL),
                        data.getInt(CAT_DISP_CLR_COL)
                        )
        );
    }

    public void loadAllSavedArticles(ArticleLoadedCallback callback) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DatabaseConstants.SAVED_TABLE;
        Cursor data =  db.rawQuery(query, null);
        // Get the number of articles
        int numberOfSavedArticles = data.getCount();
        // Move to last index and loop backwards
        // So that newest additions appear first
        data.moveToLast();
        for(int i = numberOfSavedArticles-1; i>=0; i--){
            getSingleSavedArticle(callback, data);
            data.moveToPrevious();
        }
        data.close();
    }

    /**
     * https://stackoverflow.com/questions/5703330/saving-arraylists-in-sqlite-databases
     * @param str
     * @return
     */
    private static final String JSON_str = "iPaths";
    private static String convertArrayToString(String[] array){
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray(new ArrayList<>(Arrays.asList(array)));
        try {
            json.put(JSON_str,jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private static String[] convertStringToArray(String str){
        JSONObject json = null;
        try {
            json = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = json.optJSONArray(JSON_str);
        String[] strArr = new String[jsonArray.length()];
        for(int i = 0; i < jsonArray.length(); i++)
            strArr[i] = jsonArray.optString(i);

        return strArr;
    }

    public boolean remove(Article... articles) {
        boolean succeeded = true;

        for(Article article: articles){
            boolean isSuccessful = this.getWritableDatabase().delete(DatabaseConstants.SAVED_TABLE, ID + "=" + "'" + article.getArticleID() + "'" , null) > 0;
            succeeded = succeeded && isSuccessful;
        }
        return succeeded;
    }


    public interface ArticleLoadedCallback {
        void onArticleLoaded(Article article);
    }
}
