package com.example.mall;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class Config extends AppCompatActivity {

    public String path = "http://192.168.1.176:8090/";

    public String getPath() {
        //SharedPreferences sp =getSharedPreferences("data",MODE_PRIVATE);

        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
