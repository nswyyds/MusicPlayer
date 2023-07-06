package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    //数据库名字
    public static final String DB_NAME = "MyLove.db";
    public static final String TABLE_NAME="mylovemusic";
    public static final int VERSION=1;
    private static final String CREATE_USER = "create table mylovemusic(id integer primary key autoincrement," +
            "title text," +
            "author text," +
            "url text)";
    //创建用户喜欢表


    public MySQLiteOpenHelper(@Nullable Context context) {
        super(context,DB_NAME,null,1);
    }
    public MySQLiteOpenHelper(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){
        super(context, dbname, factory, version);
    }
    //创建数据表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public long addMyLove(Music mylove){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title",mylove.title);
        cv.put("author",mylove.author);
        cv.put("url",mylove.url);
        long myloved = db.insert(TABLE_NAME,null,cv);
        return myloved;
    }

    public void deleteMyLove(Music mylove){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "title=?", new String[]{String.valueOf(mylove.title)});
    }

    public boolean isLove(Music mylove){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,null,"title=?",new String[]{String.valueOf(mylove.title)},null,null,null);
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }

    }

    public Music selectLove(Music mymusic){
        SQLiteDatabase db = getWritableDatabase();
        String title,author,url;
        Music result = null;
        Cursor cursor=db.query(TABLE_NAME,null,"title=?",new String[]{String.valueOf(mymusic.title)},null,null,null);
        if(cursor.moveToFirst()){
            title = cursor.getString(cursor.getColumnIndex("title"));
            author = cursor.getString(cursor.getColumnIndex("author"));
            url = cursor.getString(cursor.getColumnIndex("url"));
            result = new Music(title,author,url);
        }


        return result;

    }

}