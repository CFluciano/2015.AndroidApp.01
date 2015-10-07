package de.cyberforum.openit_project_20151;

import android.provider.BaseColumns;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.io.File;

public class DatabaseOperations extends SQLiteOpenHelper{

    public static final int database_version = 1;

    public String CREATE_TAB_NEWS = "CREATE TABLE "+ TableInfo.TABLE_NAME + "("+ TableInfo.ID_NEWS_PK+" INTEGER,"
                                                                                + TableInfo.CAT_NEWS+" TEXT,"
                                                                                + TableInfo.DATE_NEWS+" TEXT,"
                                                                                + TableInfo.NEWS_TITLE+" TEXT,"
                                                                                + TableInfo.NEWS+" TEXT,"
                                                                                + TableInfo.FLAG_READ+" TEXT,"
                                                                                + TableInfo.FLAG_DELETE+" TEXT,"
                                                                                + TableInfo.FLAG_REMEMBER+" TEXT,"
                                                                                + TableInfo.MARK+" TEXT"
                                                                                + ");";
    public String DROP_TAB_NEWS = "DROP TABLE "+ TableInfo.TABLE_NAME;

    protected DatabaseOperations(Context context) {
        super(context, TableInfo.DATABASE_NAME, null, database_version);
    }

    public void onCreate (SQLiteDatabase sdb)
    {
        sdb.execSQL(CREATE_TAB_NEWS);
    }

    public void onUpgrade (SQLiteDatabase arg0, int arg1, int arg2)
    {
    }
    protected void droptabella(SQLiteDatabase SQ)
    {
        // SQ.execSQL("DROP TABLE "+ TableInfo.TABLE_NAME);
        SQ.execSQL("DROP TABLE news_list");
    }

    public void putInformation(DatabaseOperations dop, int pk_id, String date_integer, String news)
    {
        SimpleDateFormat todays_date = new SimpleDateFormat();
        todays_date.applyPattern("dd/MM/yyyy");

        SQLiteDatabase SQ = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableInfo.ID_NEWS_PK, pk_id);
        cv.put(TableInfo.CAT_NEWS, "CF");
        cv.put(TableInfo.DATE_NEWS, date_integer);        //              String.valueOf(todays_date));
        cv.put(TableInfo.NEWS_TITLE, news.substring(0, 2));
        cv.put(TableInfo.NEWS, news);
        cv.put(TableInfo.FLAG_READ, "N");
        cv.put(TableInfo.FLAG_DELETE, "N");
        cv.put(TableInfo.FLAG_REMEMBER, "Y");
        cv.put(TableInfo.MARK, "0");
        SQ.insert(TableInfo.TABLE_NAME, null, cv);
    }

    public Cursor getAllData () {
        SQLiteDatabase SQ = this.getWritableDatabase();
        Cursor C = SQ.rawQuery("select * from " + TableInfo.TABLE_NAME,null);
        return C;
    }

    public Cursor getNews (SQLiteDatabase db) {
        Cursor cursor;

        SQLiteDatabase SQ = this.getReadableDatabase();

        String [] columns = {TableInfo.ID_NEWS_PK,TableInfo.DATE_NEWS,TableInfo.NEWS};
        String selection = TableInfo.CAT_NEWS+ "= ?";
        String [] selection_args = {};
        cursor = SQ.query(TableInfo.TABLE_NAME, columns , null,null,null,null,null);
        return cursor;
    }

    public static class  TableInfo implements BaseColumns
    {
        public static final String ID_NEWS_PK = "id_news_pk";
        public static final String CAT_NEWS = "cat_news";
        public static final String DATE_NEWS = "date_news";
        public static final String NEWS_TITLE = "news_title";
        public static final String NEWS = "news";
        public static final String FLAG_READ = "flag_read";
        public static final String FLAG_DELETE = "flag_delete";
        public static final String FLAG_REMEMBER = "flag_remember";
        public static final String MARK = "mark";
        public static final String DATABASE_NAME = "cf_news_new";
        public static final String TABLE_NAME = "news_list_new";

    }
}
