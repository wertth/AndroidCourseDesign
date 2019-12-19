package com.example.mall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.example.mall.R;

public class settingActivity extends AppCompatActivity implements View.OnClickListener{
    private Button logout;
    //private Button reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);
        findViewById(R.id.reg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                setLogout();
                finish();
                break;
            case R.id.reg:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
        }
    }

    public void setLogout() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("isLogin");
        editor.putString("isLogin","false");

        editor.commit();
    }
}
