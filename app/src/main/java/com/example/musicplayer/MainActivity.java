package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    public static MySQLiteOpenHelper dbHelper;
    ListView listView;
    ArrayAdapter adapter;

    List<String> bendiList = new ArrayList<>();
    private List<String> namelist = new ArrayList<>();//歌曲名称数组
    private List<String> artistlist = new ArrayList<>();//艺术家名称数组
    private List<String> urllist = new ArrayList<>();//路径数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bendiList = getAllFileNames(getFilesDir()+"/");
        removeMp3Extension(bendiList);
        namelist.add("The Pop Winds");
        namelist.add("Jingle Bells");
        namelist.add("Into the Unknown");
        namelist.add("Ethos");
        namelist.add("Oh Radiant One");
        namelist.add("Ergo Phizmiz and the Midnight Florists");
        int x = insertUniqueItems(namelist,bendiList);
        artistlist.add("The Turquoise");
        artistlist.add("Scott Holmes");
        artistlist.add("Coalescense");
        artistlist.add("Ken Hamm");
        artistlist.add("Siddhartha");
        artistlist.add("Encore Une Fois");
        for(int j = 0 ; j < x; j++){
            artistlist.add(null);
        }
        String url1 = "https://files.freemusicarchive.org/storage-freemusicarchive-org/music/no_curator/The_Pop_Winds/The_Turquoise/The_Pop_Winds_-_05_-_The_Turquoise.mp3?download=1&name=The%20Pop%20Winds%20-%20The%20Turquoise.mp3";
        String url2 = "https://files.freemusicarchive.org/storage-freemusicarchive-org/tracks/99BX9qioxvFxilJgANMmPYcuG5I3IxTGchSFa1v8.mp3?download=1&name=Scott%20Holmes%20Music%20-%20Jingle%20Bells.mp3";
        String url3 = "https://files.freemusicarchive.org/storage-freemusicarchive-org/tracks/JuG1OD1wm6f3XD93haM4qSlwAfyBS4zrOcXstBPb.mp3?download=1&name=Maarten%20Schellekens%20-%20Into%20the%20Unknown.mp3";
        String url4 = "https://files.freemusicarchive.org/storage-freemusicarchive-org/tracks/iuQWMMFk3aB4s9zh57Nz6RZh3rQByBC4TMk0pH2J.mp3?download=1&name=Ethos%20-%20Coalescense.mp3";
        String url5 = "https://files.freemusicarchive.org/storage-freemusicarchive-org/tracks/mV3CPsPAboQ1mA4Ji0P2OiKeBNCsy3nPzJGys9l8.mp3?download=1&name=Siddhartha%20Corsus%20-%20Oh%20Radiant%20One.mp3";
        String url6 = "https://files.freemusicarchive.org/storage-freemusicarchive-org/tracks/mV3CPsPAboQ1mA4Ji0P2OiKeBNCsy3nPzJGys9l8.mp3?download=1&name=Siddhartha%20Corsus%20-%20Oh%20Radiant%20One.mp3";
        //添加url到urllist
        urllist.add(url1);
        urllist.add(url2);
        urllist.add(url3);
        urllist.add(url4);
        urllist.add(url5);
        urllist.add(url6);
        for(int j = 0 ; j < x; j++){
            urllist.add(null);
        }
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, namelist);//数组适配器，显示歌曲名
        listView = findViewById(R.id.listview);
        listView.setAdapter(adapter);//给listview设置适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = namelist.get(position);
                Intent intent = new Intent(MainActivity.this, MusicPlayer.class);
                //因为intent只能不能传List，只能传String，因此需要转换
                String[] nameList = new String[6 + x];
                for (int i = 0; i < 6 + x; i++) {
                    nameList[i] = namelist.get(i);
                }
                String[] artistList = new String[6 + x];
                for (int i = 0; i < 6 + x; i++) {
                    artistList[i] = artistlist.get(i);
                }
                String[] urlStr = new String[6 + x];
                for (int i = 0; i < 6 + x; i++) {
                    urlStr[i] = urllist.get(i);
                }
                //通过intent把歌曲id、歌曲名、艺术家名和路径传给MusicPlayer
                intent.putExtra("position", position);
                intent.putExtra("artistList", artistList);
                intent.putExtra("nameList", nameList);
                intent.putExtra("urlStr", urlStr);
                intent.putExtra("length", 6 + x);
                startActivity(intent);//开启活动，音乐播放
                finish();
            }
        });
    }
    public static List<String> getAllFileNames(String directoryPath) {
        List<String> fileNames = new ArrayList<>();

        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
            }
        }

        return fileNames;
    }

    public static long getMediaDuration(String filePath) {
        long duration = 0;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(filePath);
            String durationStr = mediaMetadataRetriever
                    .extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (durationStr != null) {
                duration = Long.parseLong(durationStr);
            }
        } catch (IllegalArgumentException e) {

        } finally {
            mediaMetadataRetriever.release();
        }
        return duration;
    }

    public static void removeMp3Extension(List<String> bendiList) {
        for (int i = 0; i < bendiList.size(); i++) {
            String fileName = bendiList.get(i);
            if (fileName.endsWith(".mp3")) {
                fileName = fileName.substring(0, fileName.length() - 4);
                bendiList.set(i, fileName);
            }
        }
    }

    public static int insertUniqueItems(List<String> namelist, List<String> bendiList) {
        int i = 0;
        for (String item : bendiList) {
            if (!namelist.contains(item)) {
                namelist.add(item);
                i++;
            }
        }
        return i;
    }
}