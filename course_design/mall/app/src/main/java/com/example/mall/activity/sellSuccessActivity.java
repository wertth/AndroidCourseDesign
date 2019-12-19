package com.example.mall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.example.mall.MainActivity;
import com.example.mall.R;

import java.util.Timer;
import java.util.TimerTask;

public class sellSuccessActivity extends AppCompatActivity {

    private TextView tv_time;
    private int reclen=4;
    Timer timer=new Timer();

    TimerTask  task=new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reclen--;
                    tv_time.setText("交易成功,"+reclen+"s后返回到首页");
                    if (reclen<0){
                        timer.cancel();
                        tv_time.setVisibility(View.GONE);
                        startActivity(new Intent(sellSuccessActivity.this,MainActivity.class));
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_sell_success);
        tv_time = findViewById(R.id.sell_seccess);
//        try {
//            //Thread.sleep(3000); //设置暂停的时间 3 秒
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        timer.schedule(task,1000,1000);
//        startActivity(new Intent(this, MainActivity.class));
    }
}
