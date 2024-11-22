package com.example.notepadapp.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notepadapp.bean.Notepad;

import java.util.ArrayList;
import java.util.List;

//数据库帮助类
public class MyDbHelper extends SQLiteOpenHelper {

    private static final int version = 1;

    private static final String name = "notepad.db";
    public MyDbHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table notepad(id integer primary key autoincrement,content text,time text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //新增一条记录
    public void insert(Notepad notepad)
    {
        String sql = "insert into notepad(content,time) values(?,?)";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql, new String[]{notepad.getContent(), notepad.getTime()});
    }

    //查询所有记录
    @SuppressLint("Range")
    public List<Notepad> findAll() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from notepad";
        Cursor cursor = db.rawQuery(sql, null);
        List<Notepad> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Notepad notepad = new Notepad();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            notepad.setId(id);
            notepad.setContent(content);
            notepad.setTime(time);
            list.add(notepad);
        }
        cursor.close();
        return list;
    }

    public void delete(Notepad notepad) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from notepad where id = ?";
        db.execSQL(sql, new String[]{String.valueOf(notepad.getId())});
    }

    public void update(Notepad notepad) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "update notepad set content = ?, time = ? where id = ?";
        db.execSQL(sql, new Object[]{notepad.getContent(), notepad.getTime(),  notepad.getId()});
    }
}
