package com.example.mall.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.example.mall.Config;
import com.example.mall.R;
import com.example.mall.TakePhoto;
import com.example.mall.entity.User;
import okhttp3.*;

import java.io.IOException;

import static com.example.mall.utils.MD5.md5;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button submit;

    public Button btn_face_login;

    public Config config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        config = new Config();
        submit.setOnClickListener(this);
        btn_face_login = findViewById(R.id.btn_face_login);
        btn_face_login.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(getLogin()) finish();
    }
    public boolean getLogin(){
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String data = sp.getString("isLogin","");
        Log.e("isLogin() ===  ",data);
        if(data.equals("true")) return true;
        else return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                //if(!username.getText().toString().equals("") && !password.getText().toString().equals("")){
                //   setLogin(username.getText().toString(), password.getText().toString());
                //}
                doPostUser(username.getText().toString().trim(), md5(password.getText().toString().trim()));
                //startActivity(new Intent(this, MineFragment.class));
                //finish();
                break;
            case R.id.btn_face_login:
                startActivity(new Intent(this, TakePhoto.class));
                break;
        }
    }
    public void LoginSuccess(String result) {
        User user = JSON.parseObject(result, User.class);
        setLogin(user);
        //startActivity(new Intent(this, MineFragment.class));
        finish();
    }
    public void LoginFailure() {
        Toast.makeText(this,"登陆失败，重新登录",Toast.LENGTH_SHORT).show();
    }
    private void doPostUser(String username, String password) {
        //1.获取OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造RequestBody
        FormBody body = new FormBody.Builder().add("username", username).add("password", password).build();
        Request request = new Request.Builder().url(config.getPath()+"api/loginWithPassword").post(body).build();
        //3.将Request封装为Call对象
        Call call = okHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("", "onFailure: " + e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.e("postUser", "onResponse: " + result);
                //runOnUiThread(() -> ToastUtil.showAtOnce(this, result));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("登录状态返回：",result);
                        if(result.equals("false"))
                            LoginFailure();
                        else LoginSuccess(result);
                    }
                });
            }
        });
    }

    public void setLogin(User user){
        String username = user.getUsername().trim();
        String password = user.getPassword();
        String logo = user.getLogo();
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("isLogin","true");
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("logo",logo);
        editor.putString("address",user.getAddress());
        editor.putString("addr",user.getAddr());
        editor.putString("tel",user.getTel());
        editor.commit();
    }
}
