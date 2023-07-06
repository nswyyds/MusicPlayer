package com.example.musicplayer;

import static com.example.musicplayer.MySQLiteOpenHelper.DB_NAME;
import static com.example.musicplayer.MySQLiteOpenHelper.TABLE_NAME;
import static com.example.musicplayer.MySQLiteOpenHelper.VERSION;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class MyLove extends AppCompatActivity {
    public static MySQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private List<String> mylovemusiclist = new ArrayList<>();
    ListView listView;
    ArrayAdapter<String> adapter;
    Button mylove_more;
    List<String> titlelist,authorlist,urllist;
    public static MySQLiteOpenHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        titlelist = new ArrayList<>();
        authorlist = new ArrayList<>();
        urllist = new ArrayList<>();
        dbHelper = new MySQLiteOpenHelper(MyLove.this, DB_NAME, null, VERSION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_love);
        mylove_more = findViewById(R.id.add_button);
        dbHelper.getWritableDatabase();
        listView = findViewById(R.id.listloveview);
        init();
        mylove_more.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MyLove.this,MainActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = titlelist.get(position);
                Intent intent = new Intent(MyLove.this,MusicPlayer.class);

                String[] nameList=new String[20];
                for(int i=0;i<titlelist.size();i++){
                    nameList[i]=titlelist.get(i);
                }
                String[] artistList= new String[20];
                for(int i=0;i<authorlist.size();i++){
                    artistList[i]=authorlist.get(i);
                }
                String[] urlStr = new String[20];
                for(int i=0;i<urllist.size();i++){
                    urlStr[i]=urllist.get(i);
                }
                //通过intent把歌曲id、歌曲名、艺术家名和路径传给MusicPlayer
                intent.putExtra("position",position);
                intent.putExtra("artistList",artistList);
                intent.putExtra("nameList",nameList);
                intent.putExtra("urlStr",urlStr);
                intent.putExtra("length",titlelist.size());
                startActivity(intent);//开启活动，音乐播放
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent  data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();

    }
    private void init() {
        db = dbHelper.getWritableDatabase();
        mylovemusiclist.clear();
        //查询数据库，将title一列添加到列表项目中
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            String diary_item;
            do {
                diary_item = cursor.getString(cursor.getColumnIndex("title"));
                titlelist.add(cursor.getString(cursor.getColumnIndex("title")));
                authorlist.add(cursor.getString(cursor.getColumnIndex("author")));
                urllist.add(cursor.getString(cursor.getColumnIndex("url")));
                mylovemusiclist.add(diary_item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new ArrayAdapter<String>(
                MyLove.this, android.R.layout.simple_list_item_1, mylovemusiclist);
        listView = findViewById(R.id.listloveview);
        listView.setAdapter(adapter);
    }
}